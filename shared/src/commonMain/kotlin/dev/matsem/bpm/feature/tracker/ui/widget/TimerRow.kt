package dev.matsem.bpm.feature.tracker.ui.widget

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.unit.dp
import dev.matsem.bpm.design.theme.BpmTheme
import dev.matsem.bpm.design.theme.Grid
import dev.matsem.bpm.design.tooling.Showcase
import dev.matsem.bpm.design.tooling.centeredVertically
import dev.matsem.bpm.feature.tracker.formatting.DurationFormatter.formatForTimer
import dev.matsem.bpm.data.repo.model.Timer
import dev.matsem.bpm.data.repo.model.MockTimers
import dev.matsem.bpm.design.tooling.HorizontalSpacer
import kotlinx.coroutines.delay
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TimerRow(
    timer: Timer,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    onResume: () -> Unit,
    onPause: () -> Unit,
    onOpenDetail: () -> Unit,
    onDelete: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val focusedColor = when (isFocused) {
        true -> BpmTheme.colorScheme.surfaceContainerHigh
        false -> Color.Unspecified
    }

    Row(
        modifier = modifier
            .combinedClickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = {},
                onDoubleClick = onOpenDetail
            )
            .onKeyEvent { keyEvent ->
                if (keyEvent.type != KeyEventType.KeyDown) {
                    return@onKeyEvent false
                }

                return@onKeyEvent when {
                    keyEvent.key == Key.Backspace -> {
                        onDelete()
                        true
                    }

                    keyEvent.key == Key.Enter -> {
                        onOpenDetail()
                        true
                    }

                    keyEvent.key == Key.Spacebar -> {
                        if (timer.state.isRunning) {
                            onPause()
                        } else {
                            onResume()
                        }
                        true
                    }

                    else -> false
                }
            }
            .background(focusedColor)
            .padding(vertical = Grid.d2)
            .padding(contentPadding),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IssueTitleRow(issue = timer.issue, modifier = Modifier.weight(1f))

        // Action Row
        HorizontalSpacer(Grid.d2)
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (timer.state.duration.isPositive()) {
                var timerText by remember { mutableStateOf(timer.state.duration.formatForTimer()) }
                LaunchedEffect(timer.state.isRunning) {
                    while (timer.state.isRunning) {
                        delay(1.seconds)
                        timerText = timer.state.duration.formatForTimer()
                    }
                }

                Text(
                    text = timerText,
                    style = BpmTheme.typography.bodySmall.centeredVertically(),
                    color = BpmTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .padding(end = Grid.d2)
                        .background(
                            color = BpmTheme.colorScheme.surfaceContainerHighest,
                            shape = BpmTheme.shapes.extraSmall
                        )
                        .padding(horizontal = Grid.d1, vertical = Grid.d1)
                )
            }

            IconButton(
                onClick = {
                    if (timer.state.isRunning) {
                        onPause()
                    } else {
                        onResume()
                    }
                },
                modifier = Modifier.size(Grid.d7),
                colors = IconButtonDefaults.outlinedIconButtonColors(
                    contentColor = BpmTheme.colorScheme.onSurfaceVariant
                )
            ) {
                Crossfade(timer.state.isRunning) { isRunning ->
                    when (isRunning) {
                        true -> {
                            Icon(
                                imageVector = Icons.Rounded.Pause,
                                contentDescription = "Pause"
                            )
                        }

                        false -> {
                            Icon(
                                imageVector = Icons.Rounded.PlayArrow,
                                contentDescription = "Resume"
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
@Preview
private fun TrackerRowPreview() {

    Showcase(isDark = false) {
        Column {
            for (tracker in MockTimers) {
                TimerRow(
                    timer = tracker,
                    modifier = Modifier.fillMaxWidth(),
                    onResume = {},
                    onPause = {},
                    onOpenDetail = {},
                    onDelete = {},
                )
            }
        }
    }
}