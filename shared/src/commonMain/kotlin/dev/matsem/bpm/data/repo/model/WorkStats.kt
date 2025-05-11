package dev.matsem.bpm.data.repo.model

import kotlinx.datetime.LocalDate
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

/**
 * Represents statistics for work tracking within a specified time range.
 *
 * @property type The type of work statistics, indicating whether it's calculated daily, weekly, or for the current period.
 * @property dateRange The range of dates the statistics cover.
 * @property requiredDuration The total required duration of work for the specified time range.
 * @property trackedDuration The total duration of work that was tracked within the specified time range.
 */
data class WorkStats(
    val type: Type,
    val dateRange: ClosedRange<LocalDate>,
    val requiredDuration: Duration,
    val trackedDuration: Duration,
) {
    enum class Type {
        Today,
        ThisWeek,
        CurrentPeriod
    }

    val percent: Float
        get() {
            if (requiredDuration.inWholeSeconds == 0L) {
                return 0f
            }
            return (trackedDuration.inWholeSeconds / requiredDuration.inWholeSeconds.toFloat()).coerceIn(0f..1f)
        }

    val overtime: Duration
        get() = (trackedDuration - requiredDuration).coerceAtLeast(0.seconds)
}
