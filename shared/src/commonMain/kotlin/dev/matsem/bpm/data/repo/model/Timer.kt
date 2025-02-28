package dev.matsem.bpm.data.repo.model

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

data class Timer(
    val id: Int,
    val issue: Issue,
    val createdAt: Instant,
    val state: TimerState = TimerState(),
)

/**
 * State of the time tracker.
 *
 * @param finishedDuration Duration already tracked into this timer, not including any running timer.
 * @param lastStartedAt If timer is running, contains an [Instant] of when the timer was started.
 */
data class TimerState(
    val finishedDuration: Duration = 0.seconds,
    val lastStartedAt: Instant? = null,
) {
    val isRunning: Boolean
        get() = lastStartedAt != null

    /**
     * A total duration of timer, including all previously tracked time + currently tracked time.
     */
    val duration: Duration
        get() {
            val runningDuration = lastStartedAt?.let {
                Clock.System.now() - lastStartedAt
            } ?: 0.seconds

            return finishedDuration + runningDuration
        }
}

val MockTimers = MockIssues.map { Timer(id = 0, issue = it, createdAt = Clock.System.now()) }