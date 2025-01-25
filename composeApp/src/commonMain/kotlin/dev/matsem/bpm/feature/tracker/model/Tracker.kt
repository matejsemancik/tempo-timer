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

data class TrackerLog(
    val start: Instant,
    val end: Instant,
) {
    init {
        require(end > start) { "end time must be greater than start time" }
    }
}

data class TrackerState(
    val finishedLogs: List<TrackerLog> = emptyList(),
    val startedAt: Instant? = null
) {
    val isRunning: Boolean
        get() = startedAt != null

    val duration: Duration
        get() {
            val finishedLogsDuration = finishedLogs.map {
                it.end.minus(it.start)
            }.fold(0.seconds) { acc, duration -> acc + duration}

            val runningDuration = startedAt?.let {
                Clock.System.now() - startedAt
            } ?: 0.seconds

            return finishedLogsDuration + runningDuration
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
            finishedLogs = listOf(
                TrackerLog(start = Clock.System.now().minus(20.minutes), end = Clock.System.now())
            )
        )
    ),
    Tracker(
        issue = Issue(
            type = IssueType.Subtask,
            key = "MTSM-19",
            title = "[AN] Spraviť robotu",
        )
    ),
    Tracker(
        issue = Issue(
            type = IssueType.Story,
            key = "MTSM-140",
            title = "Robenie roboty",
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