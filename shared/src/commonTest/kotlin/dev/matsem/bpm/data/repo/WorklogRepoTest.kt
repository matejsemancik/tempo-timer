package dev.matsem.bpm.data.repo

import dev.matsem.bpm.data.repo.model.WorkStats
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.hours

class WorklogRepoTest {

    @Test
    fun `given no tracked time, when stats are synced, then stats flow will contain empty stats`() =
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
                    trackedDuration = 0.hours
                )
            )
            assertContains(
                iterable = workStats,
                element = WorkStats(
                    type = WorkStats.Type.ThisWeek,
                    dateRange = LocalDate.parse("2025-06-02")..LocalDate.parse("2025-06-08"),
                    requiredDuration = 40.hours,
                    trackedDuration = 0.hours
                )
            )
            assertContains(
                iterable = workStats,
                element = WorkStats(
                    type = WorkStats.Type.CurrentPeriod,
                    dateRange = LocalDate.parse("2025-06-01")..LocalDate.parse("2025-06-30"),
                    requiredDuration = 168.hours,
                    trackedDuration = 0.hours
                )
            )
        }
}
