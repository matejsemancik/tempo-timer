package dev.matsem.bpm.feature.tracker.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.matsem.bpm.design.theme.BpmTheme
import dev.matsem.bpm.tracker.model.IssueType
import dev.matsem.bpm.design.tooling.Showcase
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun IssueTypeIcon(
    type: IssueType,
    modifier: Modifier = Modifier
) {
    Icon(
        imageVector = type.icon,
        tint = Color.White,
        modifier = modifier
            .background(color = type.color, shape = BpmTheme.shapes.small)
            .padding(4.dp),
        contentDescription = null
    )
}

@Preview
@Composable
fun IssueTypeIconPreview() {
    val iconSize = 32.dp
    Showcase {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            IssueTypeIcon(IssueType.Bug, Modifier.size(iconSize))
            IssueTypeIcon(IssueType.Task, Modifier.size(iconSize))
            IssueTypeIcon(IssueType.Subtask, Modifier.size(iconSize))
            IssueTypeIcon(IssueType.Story, Modifier.size(iconSize))
            IssueTypeIcon(IssueType.Epic, Modifier.size(iconSize))
        }
    }
}