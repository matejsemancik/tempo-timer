package dev.matsem.bpm.feature.commit.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.matsem.bpm.data.repo.model.MockTimers
import dev.matsem.bpm.design.chip.AppSuggestionChip
import dev.matsem.bpm.design.input.AppTextField
import dev.matsem.bpm.design.input.LabeledTextField
import dev.matsem.bpm.design.theme.BpmTheme
import dev.matsem.bpm.design.theme.Grid
import dev.matsem.bpm.design.tooling.HorizontalSpacer
import dev.matsem.bpm.design.tooling.Showcase
import dev.matsem.bpm.design.tooling.VerticalSpacer
import dev.matsem.bpm.feature.commit.presentation.CommitActions
import dev.matsem.bpm.feature.commit.presentation.CommitScreen
import dev.matsem.bpm.feature.commit.presentation.CommitState
import dev.matsem.bpm.feature.tracker.ui.widget.LargeIssueTitleRow
import kotlinx.collections.immutable.persistentListOf
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
        LabeledTextField(label = "⏳ Duration") {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AppTextField(
                    value = state.durationInput,
                    isError = state.isDurationInputError,
                    placeholder = "e.g. \"1h 20m\" or \"30m\" ",
                    onValueChange = actions::onDurationInput,
                    singleLine = true,
                    modifier = Modifier.weight(1f).focusRequester(focusRequester),
                )

                if (state.durationSuggestions.isNotEmpty()) {
                    HorizontalSpacer(Grid.d2)
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(Grid.d1_5),
                    ) {
                        state.durationSuggestions.forEach {
                            AppSuggestionChip(
                                onClick = { actions.onSuggestionClick(it) },
                                label = it,
                                modifier = Modifier.sizeIn(minHeight = Grid.d11)
                            )
                        }
                    }
                }
            }
        }

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
    }
}

@Preview
@Composable
fun CommitScreenPreview() {
    Showcase {
        CommitScreenUi(
            state = CommitState(
                timer = MockTimers.first(),
                durationSuggestions = persistentListOf(
                    "1h",
                    "30m",
                    "4h 50m"
                )
            ),
            actions = CommitActions.noOp(),
            modifier = Modifier.fillMaxWidth()
        )
    }
}