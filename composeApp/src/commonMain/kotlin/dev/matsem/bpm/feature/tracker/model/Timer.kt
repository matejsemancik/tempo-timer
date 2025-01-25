package dev.matsem.bpm.feature.tracker.model

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

data class Timer(
    val issue: Issue?,
    val state: TimerState = TimerState()
)

/**
 * State of the time tracker.
 *
 * @param finishedDuration Duration already tracked into this timer, not including any running timer.
 * @param startedAt If timer is running, contains an [Instant] of when the timer was started.
 */
data class TimerState(
    val finishedDuration: Duration = 0.seconds,
    val startedAt: Instant? = null
) {
    val isRunning: Boolean
        get() = startedAt != null

    /**
     * A total duration of timer, including all previously tracked time + currently tracked time.
     */
    val duration: Duration
        get() {
            val runningDuration = startedAt?.let {
                Clock.System.now() - startedAt
            } ?: 0.seconds

            return finishedDuration + runningDuration
        }
}

val TimerMocks = listOf(
    Timer(
        issue = Issue(
            type = IssueType.Bug,
            key = "MTSM-1",
            title = "Dojebaný payment button",
        ),
    ),
    Timer(
        issue = Issue(
            type = IssueType.Task,
            key = "MTSM-4",
            title = "Spraviť robotu",
        ),
    ),
    Timer(
        issue = Issue(
            type = IssueType.Subtask,
            key = "MTSM-19",
            title = "[AN] Spraviť robotu",
        ),
    ),
    Timer(
        issue = Issue(
            type = IssueType.Story,
            key = "MTSM-140",
            title = "Robenie roboty",
        ),
    ),
    Timer(
        issue = Issue(
            type = IssueType.Epic,
            key = "MTSM-5",
            title = "Tempo Desktop",
        )
    ),
)