package dev.matsem.bpm.feature.tracker.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.matsem.bpm.design.theme.BpmTheme
import dev.matsem.bpm.tracker.model.IssueType
import dev.matsem.bpm.feature.tracker.model.Tracker
import dev.matsem.bpm.design.tooling.Showcase
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.time.Duration.Companion.minutes

@Composable
fun TrackerRow(
    tracker: Tracker,
    modifier: Modifier = Modifier,
) {
    Row(modifier.background(BpmTheme.colorScheme.surface)) {
        IssueTypeIcon(tracker.issueType, Modifier.size(32.dp))
        Text(
            text = tracker.issueKey,
            color = BpmTheme.colorScheme.outline
        )
    }
}


@Composable
@Preview
private fun TimerRowPreview() {
    val tracker = Tracker(
        issueType = IssueType.Bug,
        issueKey = "MTSM-1",
        issueTitle = "Working on this app",
        duration = 43.minutes,
        isRunning = false
    )

    Showcase(isDark = true) {
        TrackerRow(
            tracker = tracker,
            modifier = Modifier.fillMaxWidth()
        )
    }
}