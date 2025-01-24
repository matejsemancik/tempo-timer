package dev.matsem.bpm.feature.tracker.model

import dev.matsem.bpm.tracker.model.IssueType
import kotlin.time.Duration

data class Tracker(
    val issueType: IssueType,
    val issueKey: String,
    val issueTitle: String,
    val duration: Duration,
    val isRunning: Boolean,
) {

    val id: String
        get() = issueKey
}