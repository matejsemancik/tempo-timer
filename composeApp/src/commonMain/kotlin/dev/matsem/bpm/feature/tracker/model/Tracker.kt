package dev.matsem.bpm.feature.tracker.model

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

data class Tracker(
    val issue: Issue?,
    val state: TrackerState = TrackerState()
)

/**
 * State of the time tracker.
 *
 * @param finishedDuration Duration already tracked into this tracker, not including any running timer
 * @param startedAt If timer is running, contains an [Instant] of when the timer was started.
 */
data class TrackerState(
    val finishedDuration: Duration = 0.seconds,
    val startedAt: Instant? = null
) {
    val isRunning: Boolean
        get() = startedAt != null

    /**
     * A total duration of Tracker, including all previous timers + any currently running timer.
     */
    val duration: Duration
        get() {
            val runningDuration = startedAt?.let {
                Clock.System.now() - startedAt
            } ?: 0.seconds

            return finishedDuration + runningDuration
        }
}

val TrackerMock = listOf(
    Tracker(
        issue = Issue(
            type = IssueType.Bug,
            key = "MTSM-1",
            title = "Dojebaný payment button",
        ),
        state = TrackerState(
            startedAt = Clock.System.now().minus(10.seconds)
        )
    ),
    Tracker(
        issue = Issue(
            type = IssueType.Task,
            key = "MTSM-4",
            title = "Spraviť robotu",
        ),
        state = TrackerState(
            finishedDuration = 10.minutes,
        )
    ),
    Tracker(
        issue = Issue(
            type = IssueType.Subtask,
            key = "MTSM-19",
            title = "[AN] Spraviť robotu",
        ),
        state = TrackerState(
            finishedDuration = 60.minutes,
        )
    ),
    Tracker(
        issue = Issue(
            type = IssueType.Story,
            key = "MTSM-140",
            title = "Robenie roboty",
        ),
        state = TrackerState(
            finishedDuration = 120.minutes + 30.seconds,
        )
    ),
    Tracker(
        issue = Issue(
            type = IssueType.Epic,
            key = "MTSM-5",
            title = "Tempo Desktop",
        )
    ),
)