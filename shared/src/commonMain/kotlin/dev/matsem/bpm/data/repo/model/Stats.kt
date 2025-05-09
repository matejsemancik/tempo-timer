package dev.matsem.bpm.data.repo.model

import kotlinx.datetime.LocalDate
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

sealed interface Stats {
    val dateStart: LocalDate
    val dateEnd: LocalDate
    val requiredDuration: Duration
    val trackedDuration: Duration

    val percent: Float
        get() = (trackedDuration.inWholeSeconds / requiredDuration.inWholeSeconds.toFloat()).coerceIn(0f..1f)

    val overtime: Duration
        get() = (trackedDuration - requiredDuration).coerceAtLeast(0.seconds)
}

data class CurrentWeekStats(
    override val dateStart: LocalDate,
    override val dateEnd: LocalDate,
    override val requiredDuration: Duration,
    override val trackedDuration: Duration,
) : Stats

data class CurrentPeriodStats(
    override val dateStart: LocalDate,
    override val dateEnd: LocalDate,
    override val requiredDuration: Duration,
    override val trackedDuration: Duration,
) : Stats
