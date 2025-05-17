package dev.matsem.bpm.feature.logbook.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.matsem.bpm.design.theme.BpmTheme
import dev.matsem.bpm.feature.logbook.presentation.LogbookActions
import dev.matsem.bpm.feature.logbook.presentation.LogbookScreen
import dev.matsem.bpm.feature.logbook.presentation.LogbookState
import org.koin.compose.koinInject

@Composable
fun LogbookScreenUi(
    modifier: Modifier = Modifier,
    screen: LogbookScreen = koinInject(),
) {
    val state by screen.state.collectAsStateWithLifecycle()
    LogbookScreenUi(state, screen.actions, modifier)
}

@Composable
fun LogbookScreenUi(
    state: LogbookState,
    actions: LogbookActions,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Logbook Screen",
            style = BpmTheme.typography.titleLarge
        )
    }
}
