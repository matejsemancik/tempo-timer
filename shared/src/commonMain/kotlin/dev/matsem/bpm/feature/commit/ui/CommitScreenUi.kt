package dev.matsem.bpm.feature.commit.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.matsem.bpm.design.tooling.Showcase
import dev.matsem.bpm.feature.commit.presentation.CommitActions
import dev.matsem.bpm.feature.commit.presentation.CommitScreen
import dev.matsem.bpm.feature.commit.presentation.CommitState
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject

@Composable
fun CommitScreenUi(
    screen: CommitScreen = koinInject(),
    modifier: Modifier = Modifier
) {
    val state by screen.state.collectAsStateWithLifecycle()
    CommitScreenUi(
        state = state,
        actions = screen.actions,
        modifier = modifier
    )
}

@Composable
fun CommitScreenUi(
    state: CommitState,
    actions: CommitActions,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        Text("This is commit screen")
    }
}

@Preview
@Composable
fun CommitScreenPreview() {
    Showcase {
        CommitScreenUi(
            state = CommitState(),
            actions = CommitActions.noOp(),
            modifier = Modifier.fillMaxWidth()
        )
    }
}