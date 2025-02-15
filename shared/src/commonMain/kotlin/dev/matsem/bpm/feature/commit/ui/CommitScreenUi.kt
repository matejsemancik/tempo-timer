package dev.matsem.bpm.feature.commit.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.matsem.bpm.data.repo.model.MockTimers
import dev.matsem.bpm.design.input.AppTextField
import dev.matsem.bpm.design.input.LabeledTextField
import dev.matsem.bpm.design.theme.BpmTheme
import dev.matsem.bpm.design.theme.Grid
import dev.matsem.bpm.design.tooling.Showcase
import dev.matsem.bpm.design.tooling.VerticalSpacer
import dev.matsem.bpm.feature.commit.presentation.CommitActions
import dev.matsem.bpm.feature.commit.presentation.CommitScreen
import dev.matsem.bpm.feature.commit.presentation.CommitState
import dev.matsem.bpm.feature.tracker.ui.widget.LargeIssueTitleRow
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CommitScreenUi(
    state: CommitState,
    actions: CommitActions,
    modifier: Modifier = Modifier
) {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Column(modifier.padding(horizontal = BpmTheme.dimensions.horizontalContentPadding)) {
        LargeIssueTitleRow(
            issue = state.issue,
            modifier = Modifier.fillMaxWidth()
        )
        VerticalSpacer(Grid.d3)
        LabeledTextField(label = "📜 Description") {
            AppTextField(
                value = state.descriptionInput,
                onValueChange = actions::onDescriptionInput,
                placeholder = state.descriptionPlaceholder,
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )
        }

        VerticalSpacer(Grid.d3)
        LabeledTextField(label = "⏳ Duration") {
            AppTextField(
                value = state.durationInput,
                isError = state.isDurationInputError,
                placeholder = "e.g. \"1h 20m\" or \"30m\" ",
                onValueChange = actions::onDurationInput,
                singleLine = true,
                modifier = Modifier.fillMaxWidth().focusRequester(focusRequester),
            )
        }

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(Grid.d1_5),
            verticalArrangement = Arrangement.spacedBy(Grid.d1_5)
        ) {
            AppSuggestionChip(
                onClick = { actions.onDurationInput(TextFieldValue("3h"))},
                label = "3h",
            )

            AppSuggestionChip(
                onClick = { actions.onDurationInput(TextFieldValue("3h 20m"))},
                label = "3h 20m",
            )

            AppSuggestionChip(
                onClick = { actions.onDurationInput(TextFieldValue("2h 10m"))},
                label = "2h 10m",
            )
        }

        VerticalSpacer(Grid.d3)
    }
}

@Composable
fun AppSuggestionChip(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    SuggestionChip(
        modifier = modifier,
        onClick = onClick,
        label = { Text(text = label )},
        shape = BpmTheme.shapes.small,
        colors = SuggestionChipDefaults.suggestionChipColors(
            containerColor = BpmTheme.colorScheme.surfaceContainerHigh
        ),
        border = null
    )
}

@Preview
@Composable
fun CommitScreenPreview() {
    Showcase {
        CommitScreenUi(
            state = CommitState(
                timer = MockTimers.first()
            ),
            actions = CommitActions.noOp(),
            modifier = Modifier.fillMaxWidth()
        )
    }
}