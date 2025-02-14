package dev.matsem.bpm.feature.commit.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.matsem.bpm.data.repo.model.MockTimers
import dev.matsem.bpm.design.input.AppTextField
import dev.matsem.bpm.design.theme.BpmTheme
import dev.matsem.bpm.design.theme.Grid
import dev.matsem.bpm.design.tooling.Showcase
import dev.matsem.bpm.design.tooling.VerticalSpacer
import dev.matsem.bpm.feature.commit.presentation.CommitActions
import dev.matsem.bpm.feature.commit.presentation.CommitScreen
import dev.matsem.bpm.feature.commit.presentation.CommitState
import dev.matsem.bpm.feature.tracker.ui.widget.IssueTitleRow
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun CommitScreenUi(
    screen: CommitScreen,
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
        IssueTitleRow(
            issue = state.issue,
            modifier = Modifier.fillMaxWidth().padding(horizontal = BpmTheme.dimensions.horizontalContentPadding)
        )
        VerticalSpacer(Grid.d3)
    }
}

@Preview
@Composable
fun CommitScreenPreview() {
    Showcase {
        CommitScreenUi(
            state = CommitState(
                issue = MockTimers.first().issue
            ),
            actions = CommitActions.noOp(),
            modifier = Modifier.fillMaxWidth()
        )
    }
}