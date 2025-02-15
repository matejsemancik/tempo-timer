package dev.matsem.bpm.feature.commit.ui

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DeleteForever
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.matsem.bpm.data.repo.model.MockTimers
import dev.matsem.bpm.design.chip.AppSuggestionChip
import dev.matsem.bpm.design.button.AppButton
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
                    placeholder = "e.g. \"1h 20m\"",
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

        // Action row
        VerticalSpacer(Grid.d3)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.fillMaxWidth(),
        ) {
            AnimatedVisibility(
                visible = state.error != null,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically(),
                modifier = Modifier.weight(1f)
            ) {
                val errorText = remember { state.error ?: "" }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    CompositionLocalProvider(LocalContentColor provides BpmTheme.colorScheme.error) {
                        Icon(Icons.Rounded.Warning, contentDescription = null)
                        HorizontalSpacer(Grid.d1)
                        Text(text = errorText, style = BpmTheme.typography.bodyMedium)
                    }
                }
            }
            IconButton(
                onClick = actions::onDeleteClick,
            ) {
                Icon(Icons.Rounded.DeleteForever, contentDescription = "Delete timer")
            }
            HorizontalSpacer(Grid.d3)
            AppButton(
                text = "Log Time",
                onClick = actions::onCommitClick,
                isLoading = state.isButtonLoading
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
                ),
                error = "This is error"
            ),
            actions = CommitActions.noOp(),
            modifier = Modifier.fillMaxWidth()
        )
    }
}