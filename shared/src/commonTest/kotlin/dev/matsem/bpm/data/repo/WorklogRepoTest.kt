package dev.matsem.bpm.data.repo

import dev.matsem.bpm.data.repo.model.WorkStats
import dev.matsem.bpm.data.service.tempo.model.Worklog
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.hours

class WorklogRepoTest {

    @Test
    fun `given no tracked time, when stats are synced, then stats flow will contain correct behind stats`() =
        runTest {
            val repo = createMockWorklogRepoForWorksStats(
                at = LocalDate.parse("2025-06-02"),
                allWorklogs = emptyList()
            )

            repo.syncWorkStats()
            val workStats = repo.getWorkStats().first()
            assertEquals(3, workStats.count())
            assertContains(
                iterable = workStats,
                element = WorkStats(
                    type = WorkStats.Type.Today,
                    dateRange = LocalDate.parse("2025-06-02")..LocalDate.parse("2025-06-02"),
                    requiredDuration = 8.hours,
                    trackedDuration = 0.hours,
                    trackingDelta = (-8).hours
                )
            )
            assertContains(
                iterable = workStats,
                element = WorkStats(
                    type = WorkStats.Type.ThisWeek,
                    dateRange = LocalDate.parse("2025-06-02")..LocalDate.parse("2025-06-08"),
                    requiredDuration = 40.hours,
                    trackedDuration = 0.hours,
                    trackingDelta = (-8).hours
                )
            )
            assertContains(
                iterable = workStats,
                element = WorkStats(
                    type = WorkStats.Type.CurrentPeriod,
                    dateRange = LocalDate.parse("2025-06-01")..LocalDate.parse("2025-06-30"),
                    requiredDuration = 168.hours,
                    trackedDuration = 0.hours,
                    trackingDelta = (-8).hours
                )
            )
        }

    @Test
    fun `given tracked time putting user ahead, when stats are synced, then stats flow will contain correct ahead stats`() =
        runTest {
            val worklogs = listOf(
                // Not a real worklog, just for testing
                Worklog(
                    tempoWorklogId = 0,
                    timeSpentSeconds = 20.hours.inWholeSeconds,
                    startDate = LocalDate.parse("2025-06-02"),
                    description = "",
                    issue = Worklog.Issue(0, ""),
                    createdAt = Clock.System.now(),
                    updatedAt = Clock.System.now(),
                )
            )
            val repo = createMockWorklogRepoForWorksStats(
                at = LocalDate.parse("2025-06-03"),
                allWorklogs = worklogs
            )

            repo.syncWorkStats()
            val workStats = repo.getWorkStats().first()
            assertEquals(3, workStats.count())

            // Today stats - Tuesday
            assertContains(
                iterable = workStats,
                element = WorkStats(
                    type = WorkStats.Type.Today,
                    dateRange = LocalDate.parse("2025-06-03")..LocalDate.parse("2025-06-03"),
                    requiredDuration = 8.hours,
                    trackedDuration = 0.hours,
                    trackingDelta = (-8).hours
                )
            )

            // This week stats
            assertContains(
                iterable = workStats,
                element = WorkStats(
                    type = WorkStats.Type.ThisWeek,
                    dateRange = LocalDate.parse("2025-06-02")..LocalDate.parse("2025-06-08"),
                    requiredDuration = 40.hours,
                    trackedDuration = 20.hours,
                    trackingDelta = 4.hours
                )
            )

            // Current period stats
            assertContains(
                iterable = workStats,
                element = WorkStats(
                    type = WorkStats.Type.CurrentPeriod,
                    dateRange = LocalDate.parse("2025-06-01")..LocalDate.parse("2025-06-30"),
                    requiredDuration = 168.hours,
                    trackedDuration = 20.hours,
                    trackingDelta = 4.hours
                )
            )
        }

    @Test
    fun `given tracked time putting user behind, when stats are synced, then stats flow will contain correct behind stats`() =
        runTest {
            val worklogs = listOf(
                // Not a real worklog, just for testing
                Worklog(
                    tempoWorklogId = 0,
                    timeSpentSeconds = 10.hours.inWholeSeconds,
                    startDate = LocalDate.parse("2025-06-02"),
                    description = "",
                    issue = Worklog.Issue(0, ""),
                    createdAt = Clock.System.now(),
                    updatedAt = Clock.System.now(),
                )
            )
            val repo = createMockWorklogRepoForWorksStats(
                at = LocalDate.parse("2025-06-03"),
                allWorklogs = worklogs
            )

            repo.syncWorkStats()
            val workStats = repo.getWorkStats().first()
            assertEquals(3, workStats.count())

            // Today stats - Tuesday
            assertContains(
                iterable = workStats,
                element = WorkStats(
                    type = WorkStats.Type.Today,
                    dateRange = LocalDate.parse("2025-06-03")..LocalDate.parse("2025-06-03"),
                    requiredDuration = 8.hours,
                    trackedDuration = 0.hours,
                    trackingDelta = (-8).hours
                )
            )

            // This week stats
            assertContains(
                iterable = workStats,
                element = WorkStats(
                    type = WorkStats.Type.ThisWeek,
                    dateRange = LocalDate.parse("2025-06-02")..LocalDate.parse("2025-06-08"),
                    requiredDuration = 40.hours,
                    trackedDuration = 10.hours,
                    trackingDelta = (-6).hours
                )
            )

            // Current period stats
            assertContains(
                iterable = workStats,
                element = WorkStats(
                    type = WorkStats.Type.CurrentPeriod,
                    dateRange = LocalDate.parse("2025-06-01")..LocalDate.parse("2025-06-30"),
                    requiredDuration = 168.hours,
                    trackedDuration = 10.hours,
                    trackingDelta = (-6).hours
                )
            )
        }
}
