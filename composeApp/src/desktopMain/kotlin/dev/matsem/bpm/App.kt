package dev.matsem.bpm

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.matsem.bpm.design.theme.BpmTheme
import dev.matsem.bpm.feature.tracker.presentation.MainViewModel
import dev.matsem.bpm.feature.tracker.ui.TrackerRow
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    val viewModel = MainViewModel
    val viewState by viewModel.state.collectAsStateWithLifecycle()

    BpmTheme(isDark = false) {
        Surface(modifier = Modifier.fillMaxSize(), color = BpmTheme.colorScheme.background) {
            val scrollState = rememberScrollState()
            Column(Modifier.verticalScroll(rememberScrollState())) {
                for (tracker in viewState.trackers) {
                    TrackerRow(
                        tracker = tracker,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}