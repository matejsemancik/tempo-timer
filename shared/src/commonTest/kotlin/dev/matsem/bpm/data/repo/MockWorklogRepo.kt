package dev.matsem.bpm.data.repo

import dev.matsem.bpm.data.database.dao.UserDao
import dev.matsem.bpm.data.database.model.User
import dev.matsem.bpm.data.service.tempo.TempoApiManager
import dev.matsem.bpm.data.service.tempo.model.CreateWorklogBody
import dev.matsem.bpm.data.service.tempo.model.DaySchedule
import dev.matsem.bpm.data.service.tempo.model.TempoResultsResponse
import dev.matsem.bpm.data.service.tempo.model.TimesheetApprovalPeriod
import dev.matsem.bpm.data.service.tempo.model.Worklog
import dev.matsem.bpm.utils.readResourceFile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.serialization.json.Json

/**
 * Creates a mock implementation of [WorklogRepo] for testing purposes.
 *
 * @param at The reference date for the mock repository's clock
 * @param timeZone The timezone to use for date/time operations, defaults to Europe/Prague
 * @param approvalPeriod The date range representing the timesheet approval period
 * @param userSchedule The list of day schedules to be returned by the mock
 * @param allWorklogs The list of worklogs to be returned by the mock
 * @return A configured [WorklogRepoImpl] instance with mock dependencies
 */
internal fun createMockWorklogRepo(
    at: LocalDate,
    timeZone: TimeZone = TimeZone.of("Europe/Prague"),
    approvalPeriod: ClosedRange<LocalDate>,
    userSchedule: List<DaySchedule>,
    allWorklogs: List<Worklog>,
) = WorklogRepoImpl(
    tempoApiManager = object : TempoApiManager {
        override suspend fun getAllWorklogs(
            jiraAccountId: String, dateRange: ClosedRange<LocalDate>
        ): List<Worklog> = allWorklogs

        override suspend fun createWorklog(body: CreateWorklogBody): Worklog =
            error("not implemented")

        override suspend fun getApprovalPeriod(at: LocalDate): TimesheetApprovalPeriod =
            TimesheetApprovalPeriod(from = approvalPeriod.start, to = approvalPeriod.endInclusive)

        override suspend fun getUserSchedule(dateRange: ClosedRange<LocalDate>): List<DaySchedule> =
            userSchedule
    },
    userDao = object : UserDao {
        override suspend fun upsert(user: User) = Unit
        override suspend fun delete() = Unit
        override fun observe(): Flow<User?> = flow { User("", "", "", "") }
        override suspend fun get(): User = User("", "", "", "")
    },
    clock = object : Clock {
        override fun now(): Instant = at.atStartOfDayIn(timeZone)
    },
    systemTimeZone = timeZone
)

/**
 * Creates a mock implementation of [WorklogRepo] specifically configured for testing work statistics.
 * This helper function provides a simplified interface compared to [createMockWorklogRepo] by setting
 * default values for timezone, approval period, and user schedule.
 *
 * @param at The reference date for the mock repository's clock
 * @param allWorklogs The list of worklogs to be returned by the mock
 * @return A configured [WorklogRepoImpl] instance with mock dependencies
 */
internal fun createMockWorklogRepoForWorksStats(
    at: LocalDate,
    allWorklogs: List<Worklog>,
) = createMockWorklogRepo(
    at = at,
    allWorklogs = allWorklogs,
    timeZone = TimeZone.of("Europe/Prague"),
    approvalPeriod = LocalDate.parse("2025-06-01")..LocalDate.parse("2025-06-30"),
    userSchedule = run {
        val rawJson = readResourceFile("raw/user_schedule_2025-06.json")
        val json = Json { ignoreUnknownKeys = true }
        json.decodeFromString<TempoResultsResponse<DaySchedule>>(rawJson).results
    }
)
