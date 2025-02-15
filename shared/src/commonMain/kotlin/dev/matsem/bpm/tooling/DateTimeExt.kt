package dev.matsem.bpm.tooling

import kotlinx.datetime.LocalTime

/**
 * Returns [LocalTime] without nanoseconds.
 */
fun LocalTime.dropNanos() = LocalTime(hour, minute, second)