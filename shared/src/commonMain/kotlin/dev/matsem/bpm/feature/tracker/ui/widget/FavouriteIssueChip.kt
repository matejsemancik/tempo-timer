package dev.matsem.bpm.feature.tracker.ui.widget

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import bpm_tracker.shared.generated.resources.Res
import bpm_tracker.shared.generated.resources.remove
import dev.matsem.bpm.data.repo.model.Issue
import dev.matsem.bpm.data.repo.model.MockTimers
import dev.matsem.bpm.design.layout.ClickableAppCard
import dev.matsem.bpm.design.theme.BpmTheme
import dev.matsem.bpm.design.theme.Grid
import dev.matsem.bpm.design.tooling.HorizontalSpacer
import dev.matsem.bpm.design.tooling.Showcase
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun FavouriteIssueChip(
    issue: Issue,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    onDelete: () -> Unit,
) {
    var dropdownOffset: Offset by remember { mutableStateOf(Offset.Zero) }
    var dropdownExpanded: Boolean by remember { mutableStateOf(false) }
    var cardHeight: Int by remember { mutableStateOf(0) }

    ClickableAppCard(
        modifier = modifier
            .onKeyEvent { keyEvent ->
                if (keyEvent.type == KeyEventType.KeyDown && keyEvent.key == Key.Backspace) {
                    onDelete()
                    return@onKeyEvent true
                }
                return@onKeyEvent false
            }
            .sizeIn(maxWidth = Grid.d40)
            .onSizeChanged { size ->
                cardHeight = size.height
            },
        onClick = onClick,
        onSecondaryClick = { offset ->
            dropdownExpanded = true
            dropdownOffset = offset
        }
    ) {
        IssueTitleRow(issue = issue, modifier = Modifier.padding(horizontal = Grid.d2, vertical = Grid.d1_5))
        DropdownMenu(
            expanded = dropdownExpanded,
            onDismissRequest = { dropdownExpanded = false },
            shape = BpmTheme.shapes.small,
            tonalElevation = 0.dp,
            border = BorderStroke(Dp.Hairline, BpmTheme.colorScheme.outlineVariant),
            offset = with(LocalDensity.current) {
                DpOffset(dropdownOffset.x.toDp(), -(cardHeight - dropdownOffset.y).toDp())
            }
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clickable(
                        onClick = {
                            onDelete()
                            dropdownExpanded = false
                        }
                    )
                    .padding(horizontal = Grid.d2, vertical = Grid.d2)
            ) {
                CompositionLocalProvider(LocalContentColor provides BpmTheme.colorScheme.error) {
                    Icon(Icons.Rounded.Delete, contentDescription = null, modifier = Modifier.size(Grid.d5))
                    HorizontalSpacer(Grid.d2)
                    Text(stringResource(Res.string.remove), style = BpmTheme.typography.bodySmall)
                }
            }
        }
    }
}


@Composable
@Preview
private fun FavouriteIssueChipPreview() {

    Showcase(isDark = false) {
        Column(verticalArrangement = Arrangement.spacedBy(Grid.d1)) {
            for (issue in MockTimers.mapNotNull { it.issue }) {
                FavouriteIssueChip(
                    issue = issue,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = Grid.d4),
                    onClick = {},
                    onDelete = {}
                )
            }
        }
    }
}