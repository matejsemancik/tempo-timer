package dev.matsem.bpm.data.repo

import dev.matsem.bpm.data.database.dao.UserDao
import dev.matsem.bpm.data.repo.model.PeriodWorkStats
import dev.matsem.bpm.data.repo.model.WeeklyWorkStats
import dev.matsem.bpm.data.repo.model.Issue
import dev.matsem.bpm.data.repo.model.WorkStats
import dev.matsem.bpm.data.service.tempo.TempoApiManager
import dev.matsem.bpm.data.service.tempo.model.CreateWorklogBody
import dev.matsem.bpm.tooling.dropNanos
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
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

interface WorklogRepo {

    suspend fun createWorklog(
        jiraIssue: Issue,
        createdAt: Instant,
        timeSpent: Duration,
        description: String?,
    )

    fun getWorkStats(): Flow<List<WorkStats>>
}

internal class WorklogRepoImpl(
    private val tempoApiManager: TempoApiManager,
    private val userDao: UserDao,
    private val clock: Clock,
) : WorklogRepo {

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
            startedAtDate = createdAt.toLocalDateTime(TimeZone.currentSystemDefault()).date,
            // The current system default time zone is our best bet rn.
            startedAtTime = createdAt.toLocalDateTime(TimeZone.currentSystemDefault()).time.dropNanos(),
            timeSpentSeconds = timeSpent.inWholeSeconds,
            description = description
        )
        val worklog = tempoApiManager.createWorklog(requestBody)
        println("Worklog created: $worklog")
    }

    // TODO react to worklog changes
    override fun getWorkStats(): Flow<List<WorkStats>> = flow {
        val dateNow = clock.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        val currentApprovalPeriod = tempoApiManager.getApprovalPeriod(dateNow)
        val currentWeek: Pair<LocalDate, LocalDate> = dateNow.let { date ->
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
            val end = if (currentApprovalPeriod != null && sunday > currentApprovalPeriod.to) {
                currentApprovalPeriod.to
            } else {
                sunday
            }

            start to end
        }

        val weeklyWorkStats = getStatsForDates(currentWeek.first, currentWeek.second).let {
            WeeklyWorkStats(
                dateStart = currentWeek.first,
                dateEnd = currentWeek.second,
                requiredDuration = it.first,
                trackedDuration = it.second
            )
        }

        val periodWorkStats = currentApprovalPeriod?.let { approvalPeriod ->
            getStatsForDates(approvalPeriod.from, approvalPeriod.to).let {
                PeriodWorkStats(
                    dateStart = approvalPeriod.from,
                    dateEnd = approvalPeriod.to,
                    requiredDuration = it.first,
                    trackedDuration = it.second
                )
            }
        }

        emit(listOfNotNull(weeklyWorkStats, periodWorkStats))
    }

    /**
     * Retrieves the required and tracked durations for a user within a specified date range.
     *
     * @param from The starting date of the range for which statistics are to be retrieved.
     * @param to The ending date of the range for which statistics are to be retrieved.
     * @return A [Pair] containing two [Duration] values. The first represents the required work duration,
     * and the second represents the tracked work duration within the specified date range.
     */
    private suspend fun getStatsForDates(from: LocalDate, to: LocalDate): Pair<Duration, Duration> {
        val requiredDuration = tempoApiManager.getUserSchedule(from = from, to = to)
            .sumOf { it.requiredSeconds }.seconds

        val user = userDao.get() ?: error("No User")
        val trackedDuration = tempoApiManager.getAllWorklogs(
            jiraAccountId = user.accountId,
            from = from,
            to = to
        ).sumOf { it.timeSpentSeconds }.seconds

        return requiredDuration to trackedDuration
    }
}
