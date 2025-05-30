package dev.matsem.bpm.data.repo

import dev.matsem.bpm.data.database.dao.UserDao
import dev.matsem.bpm.data.repo.model.Issue
import dev.matsem.bpm.data.repo.model.WorkStats
import dev.matsem.bpm.data.service.tempo.TempoApiManager
import dev.matsem.bpm.data.service.tempo.model.CreateWorklogBody
import dev.matsem.bpm.data.service.tempo.model.DaySchedule
import dev.matsem.bpm.data.service.tempo.model.Worklog
import dev.matsem.bpm.tooling.dropNanos
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

/**
 * Interface for managing worklogs and retrieving related work statistics.
 * This repository provides functionalities to create a worklog, sync work statistics,
 * and observe the current work statistics such as tracked and required durations.
 */
interface WorklogRepo {

    /**
     * Creates a worklog for a specific Jira issue with the provided details.
     *
     * @param jiraIssue The Jira issue to associate the worklog with.
     * @param createdAt The timestamp when the worklog entry was created.
     * @param timeSpent The time duration spent on the Jira issue.
     * @param description An optional description for the worklog.
     */
    suspend fun createWorklog(
        jiraIssue: Issue,
        createdAt: Instant,
        timeSpent: Duration,
        description: String?,
    )

    /**
     * Updates the work statistics by synchronizing them with the latest available data.
     * This function is typically used for ensuring that the worklog and its associated statistics
     * (such as tracked and required work durations) are up-to-date. It may fetch necessary data
     * from persistent storage or external sources and process it accordingly.
     *
     * Note that the result of this synchronization is not directly returned but rather contributes
     * to the state managed by the repository.
     */
    suspend fun syncWorkStats()

    /**
     * Retrieves a stream of work statistics for tracking purposes. The emitted flow contains a list of
     * `WorkStats` objects, which represent work tracking data for specific periods (daily, weekly, or current period).
     *
     * @return A Flow emitting lists of work statistics, with each `WorkStats` containing details such as
     * required work duration, tracked duration, type of statistics (e.g., daily or weekly), and coverage date range.
     */
    fun getWorkStats(): Flow<List<WorkStats>>
}

internal class WorklogRepoImpl(
    private val tempoApiManager: TempoApiManager,
    private val userDao: UserDao,
    private val clock: Clock,
    private val systemTimeZone: TimeZone,
) : WorklogRepo {

    private val repoScope = MainScope() + SupervisorJob()
    private val workStats = MutableStateFlow(emptyList<WorkStats>())

    override suspend fun createWorklog(
        jiraIssue: Issue,
        createdAt: Instant,
        timeSpent: Duration,
        description: String?,
    ) {
        val user = userDao.get() ?: error("No User")
        val requestBody = CreateWorklogBody(
            jiraAccountId = user.accountId,
            jiraIssueId = jiraIssue.id,
            // The current system default time zone is our best bet rn.
            startedAtDate = createdAt.toLocalDateTime(systemTimeZone).date,
            // The current system default time zone is our best bet rn.
            startedAtTime = createdAt.toLocalDateTime(systemTimeZone).time.dropNanos(),
            timeSpentSeconds = timeSpent.inWholeSeconds,
            description = description
        )

        tempoApiManager.createWorklog(requestBody).also {
            println("Worklog created: $it")
        }

        repoScope.launch { runCatching { syncWorkStats() } }
    }

    override suspend fun syncWorkStats() {
        val user = userDao.get() ?: error("No user")
        val dateNow = clock.now().toLocalDateTime(systemTimeZone).date
        val currentApprovalPeriod = tempoApiManager.getApprovalPeriod(dateNow) ?: run {
            println("No active approval period")
            return
        }
        val currentWeek: ClosedRange<LocalDate> = dateNow.let { date ->
            // Find the current week's Monday (beginning of the week)
            val monday = date.minus(
                date.dayOfWeek.ordinal,
                DateTimeUnit.DAY
            )
            // Find Sunday (end of week) by adding 6 days to Monday
            val sunday = monday.plus(6, DateTimeUnit.DAY)
            monday to sunday

            // Cap end-of-week date to the end of a worklog approval period
            val start = monday
            val end = if (sunday > currentApprovalPeriod.to) {
                currentApprovalPeriod.to
            } else {
                sunday
            }

            start..end
        }

        val workSchedule = tempoApiManager.getUserSchedule(dateRange = currentApprovalPeriod.dateRange)
        val allWorklogs = tempoApiManager.getAllWorklogs(
            jiraAccountId = user.accountId,
            dateRange = currentApprovalPeriod.dateRange,
        )

        // TODO cap required duration to current period's remaining required duration
        val todayWorkStats = getStatsForDates(
            type = WorkStats.Type.Today,
            dateRange = dateNow..dateNow,
            workSchedule = workSchedule,
            worklogs = allWorklogs
        )

        // TODO cap required duration to current period's remaining required duration
        val thisWeekWorkStats = getStatsForDates(
            type = WorkStats.Type.ThisWeek,
            dateRange = currentWeek,
            workSchedule = workSchedule,
            worklogs = allWorklogs
        )

        val currentPeriodWorkStats = getStatsForDates(
            type = WorkStats.Type.CurrentPeriod,
            dateRange = currentApprovalPeriod.dateRange,
            workSchedule = workSchedule,
            worklogs = allWorklogs
        )

        workStats.update { listOf(todayWorkStats, thisWeekWorkStats, currentPeriodWorkStats) }
    }

    override fun getWorkStats(): Flow<List<WorkStats>> = workStats.asStateFlow()

    /**
     * Calculates work statistics for a specified date range.
     *
     * @param type The type of work statistics, indicating whether it's for Today, ThisWeek, or CurrentPeriod.
     * @param dateRange The range of dates for which the statistics are calculated.
     * @param workSchedule A list of day schedules containing required work durations for specific dates.
     * @param worklogs A list of worklogs containing tracked work durations and their creation dates.
     * @return A `WorkStats` object containing the calculated statistics, including required and tracked durations within the specified date range.
     */
    private fun getStatsForDates(
        type: WorkStats.Type,
        dateRange: ClosedRange<LocalDate>,
        workSchedule: List<DaySchedule>,
        worklogs: List<Worklog>,
    ): WorkStats {
        val requiredDuration = workSchedule
            .filter { it.date in dateRange }
            .sumOf { it.requiredSeconds }
            .seconds

        val trackedDuration = worklogs
            .filter { it.startDate in dateRange }
            .sumOf { it.timeSpentSeconds }
            .seconds

        return WorkStats(type, dateRange, requiredDuration, trackedDuration)
    }
}
