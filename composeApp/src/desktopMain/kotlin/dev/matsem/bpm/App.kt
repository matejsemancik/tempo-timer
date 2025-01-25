package dev.matsem.bpm

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.matsem.bpm.design.theme.BpmTheme
import dev.matsem.bpm.feature.tracker.presentation.MainComponent
import dev.matsem.bpm.feature.tracker.ui.TrackerRow
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    val state by MainComponent.state.collectAsState()
    val actions = MainComponent

    BpmTheme(isDark = false) {
        Surface(modifier = Modifier.fillMaxSize(), color = BpmTheme.colorScheme.background) {
            val scrollState = rememberScrollState()

            Box {
                Column(modifier = Modifier.verticalScroll(scrollState)) {
                    for (tracker in state.trackers) {
                        TrackerRow(
                            tracker = tracker,
                            modifier = Modifier.fillMaxWidth(),
                            onResume = { actions.onResumeTracker(tracker) },
                            onPause = { actions.onPauseTracker(tracker) },
                            onCommit = { actions.onCommitTracker(tracker) }
                        )
                    }
                }
                VerticalScrollbar(
                    adapter = rememberScrollbarAdapter(scrollState),
                    modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight()
                )
            }
        }
    }
}