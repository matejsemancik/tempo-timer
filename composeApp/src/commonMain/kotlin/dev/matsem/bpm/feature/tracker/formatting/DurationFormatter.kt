package dev.matsem.bpm.feature.tracker.formatting

import kotlin.time.Duration

internal object DurationFormatter {

    private const val SecondsInMinute = 60
    private const val MinutesInHour = 60

    fun Duration.formatForTimer(): String {
        val seconds = (inWholeSeconds % SecondsInMinute).toString().padStart(2, '0')
        val minutes = (inWholeMinutes % MinutesInHour).toString().padStart(2, '0')
        val hours = inWholeHours.takeIf { it > 0 }?.toString()?.padStart(2, '0')

        return if (hours != null) {
            "$hours:$minutes:$seconds"
        } else {
            "$minutes:$seconds"
        }
    }
}