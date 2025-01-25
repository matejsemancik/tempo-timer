package dev.matsem.bpm.feature.tracker.ui.widget

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import dev.matsem.bpm.design.theme.BpmTheme
import dev.matsem.bpm.design.theme.Grid
import dev.matsem.bpm.design.tooling.HorizontalSpacer
import dev.matsem.bpm.design.tooling.VerticalSpacer
import dev.matsem.bpm.design.tooling.centeredVertically
import dev.matsem.bpm.feature.tracker.model.Issue
import dev.matsem.bpm.feature.tracker.ui.widget.IssueTypeIcon

@Composable
fun IssueTitleRow(
    issue: Issue,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        IssueTypeIcon(issue.type, Modifier.size(Grid.d6))
        HorizontalSpacer(Grid.d2)
        Column {
            Text(
                text = issue.key,
                color = BpmTheme.colorScheme.outline,
                style = BpmTheme.typography.labelSmall.centeredVertically(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            VerticalSpacer(Grid.d0_5)
            Text(
                text = issue.title,
                color = BpmTheme.colorScheme.onSurface,
                style = BpmTheme.typography.bodySmall.centeredVertically(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }

    }
}