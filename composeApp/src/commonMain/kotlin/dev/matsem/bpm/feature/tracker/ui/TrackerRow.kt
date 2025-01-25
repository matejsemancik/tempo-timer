package dev.matsem.bpm.feature.tracker.ui

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.matsem.bpm.design.theme.BpmTheme
import dev.matsem.bpm.design.theme.Grid
import dev.matsem.bpm.design.tooling.HorizontalSpacer
import dev.matsem.bpm.design.tooling.Showcase
import dev.matsem.bpm.design.tooling.centeredVertically
import dev.matsem.bpm.feature.tracker.model.Tracker
import dev.matsem.bpm.feature.tracker.model.TrackerMock
import kotlinx.coroutines.delay
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit

@Composable
fun TrackerRow(
    tracker: Tracker,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .background(BpmTheme.colorScheme.surface)
            .clickable { }
            .padding(horizontal = Grid.d4, vertical = Grid.d2),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        tracker.issue?.let {
            IssueTitleRow(issue = tracker.issue, modifier = Modifier.weight(1f))
        }

        // Action Row
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (tracker.state.duration.isPositive()) {
                var timer by remember { mutableStateOf(tracker.state.duration.toString(DurationUnit.SECONDS)) }
                LaunchedEffect(tracker.state.isRunning) {
                    while (tracker.state.isRunning) {
                        delay(1.seconds)
                        timer = tracker.state.duration.toString(DurationUnit.SECONDS)
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

            Crossfade(tracker.state.isRunning) { isRunning ->
                when (isRunning) {
                    true -> {
                        IconButton(
                            onClick = {},
                            modifier = Modifier.size(Grid.d7),
                            colors = IconButtonDefaults.outlinedIconButtonColors(
                                contentColor = BpmTheme.colorScheme.onSurfaceVariant
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Pause,
                                contentDescription = "Pause"
                            )
                        }
                    }

                    false -> {
                        IconButton(
                            onClick = {},
                            modifier = Modifier.size(Grid.d7),
                            colors = IconButtonDefaults.outlinedIconButtonColors(
                                contentColor = BpmTheme.colorScheme.onSurfaceVariant
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.PlayArrow,
                                contentDescription = "Resume"
                            )
                        }
                    }
                }
            }

            HorizontalSpacer(Grid.d2)
            IconButton(
                onClick = {},
                modifier = Modifier.size(Grid.d7),
                colors = IconButtonDefaults.filledIconButtonColors(),
                enabled = tracker.state.duration.isPositive()
            ) {
                Icon(
                    imageVector = Icons.Rounded.Check,
                    contentDescription = "Finish"
                )
            }
        }
    }
}


@Composable
@Preview
private fun TrackerRowPreview() {

    Showcase(isDark = false) {
        Column {
            for (tracker in TrackerMock) {
                TrackerRow(
                    tracker = tracker,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}