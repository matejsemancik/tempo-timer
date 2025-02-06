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
import dev.matsem.bpm.data.model.domain.Timer
import dev.matsem.bpm.data.model.domain.TimerMocks
import kotlinx.coroutines.delay
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TimerRow(
    tracker: Timer,
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
                return@onKeyEvent when {
                    keyEvent.key == Key.Backspace && keyEvent.type == KeyEventType.KeyDown -> {
                        onDelete()
                        true
                    }

                    keyEvent.key == Key.Enter && keyEvent.type == KeyEventType.KeyDown -> {
                        onOpenDetail()
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
        tracker.issue?.let {
            IssueTitleRow(issue = tracker.issue, modifier = Modifier.weight(1f))
        }

        // Action Row
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (tracker.state.duration.isPositive()) {
                var timer by remember { mutableStateOf(tracker.state.duration.formatForTimer()) }
                LaunchedEffect(tracker.state.isRunning) {
                    while (tracker.state.isRunning) {
                        delay(1.seconds)
                        timer = tracker.state.duration.formatForTimer()
                    }
                }

                Text(
                    text = timer,
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
                    if (tracker.state.isRunning) {
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
                Crossfade(tracker.state.isRunning) { isRunning ->
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
            for (tracker in TimerMocks) {
                TimerRow(
                    tracker = tracker,
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