package dev.matsem.bpm.feature.commit.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DeleteForever
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import bpm_tracker.shared.generated.resources.Res
import bpm_tracker.shared.generated.resources.delete_timer
import bpm_tracker.shared.generated.resources.description_label
import bpm_tracker.shared.generated.resources.duration_label
import bpm_tracker.shared.generated.resources.duration_placeholder
import bpm_tracker.shared.generated.resources.log_time
import dev.matsem.bpm.arch.EventEffect
import dev.matsem.bpm.data.repo.model.MockTimers
import dev.matsem.bpm.design.button.AppButton
import dev.matsem.bpm.design.chip.AppSuggestionChip
import dev.matsem.bpm.design.input.AppTextField
import dev.matsem.bpm.design.input.LabeledTextField
import dev.matsem.bpm.design.theme.BpmTheme
import dev.matsem.bpm.design.theme.Grid
import dev.matsem.bpm.design.tooling.HorizontalSpacer
import dev.matsem.bpm.design.tooling.Showcase
import dev.matsem.bpm.design.tooling.VerticalSpacer
import dev.matsem.bpm.feature.commit.presentation.CommitActions
import dev.matsem.bpm.feature.commit.presentation.CommitEvent
import dev.matsem.bpm.feature.commit.presentation.CommitScreen
import dev.matsem.bpm.feature.commit.presentation.CommitState
import dev.matsem.bpm.feature.tracker.ui.widget.LargeIssueTitleRow
import kotlinx.collections.immutable.persistentListOf
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun CommitScreenUi(
    screen: CommitScreen,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by screen.state.collectAsStateWithLifecycle()
    CommitScreenUi(
        state = state,
        actions = screen.actions,
        modifier = modifier
    )

    EventEffect(screen.events) { event ->
        when(event) {
           CommitEvent.Dismiss -> onDismissRequest()
        }
    }
}

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
        LabeledTextField(label = stringResource(Res.string.duration_label)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AppTextField(
                    value = state.durationInput,
                    isError = state.isDurationInputError,
                    placeholder = stringResource(Res.string.duration_placeholder),
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
        LabeledTextField(label = stringResource(Res.string.description_label)) {
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
                Icon(Icons.Rounded.DeleteForever, contentDescription = stringResource(Res.string.delete_timer))
            }
            HorizontalSpacer(Grid.d3)
            AppButton(
                text = stringResource(Res.string.log_time),
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