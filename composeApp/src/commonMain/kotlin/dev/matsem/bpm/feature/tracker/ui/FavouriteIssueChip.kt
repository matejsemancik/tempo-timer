package dev.matsem.bpm.feature.tracker.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.matsem.bpm.design.layout.ClickableDesignCard
import dev.matsem.bpm.design.theme.Grid
import dev.matsem.bpm.design.tooling.Showcase
import dev.matsem.bpm.feature.tracker.model.Issue
import dev.matsem.bpm.feature.tracker.model.TrackerMock
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun FavouriteIssueChip(
    issue: Issue,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    ClickableDesignCard(
        modifier = modifier,
        onClick = onClick
    ) {
        IssueTitleRow(issue = issue, modifier = Modifier.padding(horizontal = Grid.d2, vertical = Grid.d1_5))
    }
}


@Composable
@Preview
private fun FavouriteIssueChipPreview() {

    Showcase(isDark = false) {
        Column(verticalArrangement = Arrangement.spacedBy(Grid.d1)) {
            for (issue in TrackerMock.mapNotNull { it.issue }) {
                FavouriteIssueChip(
                    issue = issue,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = Grid.d4),
                    onClick = {}
                )
            }
        }
    }
}