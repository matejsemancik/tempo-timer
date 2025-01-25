package dev.matsem.bpm

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.matsem.bpm.design.theme.BpmTheme
import dev.matsem.bpm.design.theme.Grid
import dev.matsem.bpm.design.tooling.VerticalSpacer
import dev.matsem.bpm.feature.tracker.model.Issue
import dev.matsem.bpm.feature.tracker.model.Tracker
import dev.matsem.bpm.feature.tracker.presentation.MainActions
import dev.matsem.bpm.feature.tracker.presentation.MainComponent
import dev.matsem.bpm.feature.tracker.ui.FavouriteIssueCard
import dev.matsem.bpm.feature.tracker.ui.TrackerRow
import kotlinx.collections.immutable.ImmutableList
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    val state by MainComponent.state.collectAsState()
    val actions = MainComponent

    BpmTheme(isDark = false) {
        Surface(modifier = Modifier.fillMaxSize(), color = BpmTheme.colorScheme.background) {
            val scrollState = rememberScrollState()
            Box {
                Column(modifier = Modifier.verticalScroll(scrollState)) {
                    VerticalSpacer(Grid.d4)
                    Text(
                        "tempo-desktop 0.0.1",
                        style = BpmTheme.typography.labelMedium,
                        modifier = Modifier.fillMaxWidth().padding(horizontal = Grid.d3)
                    )
                    VerticalSpacer(Grid.d4)
                    FavouritesSection(state.favouriteIssues, actions, Modifier.padding(horizontal = Grid.d3))
                    VerticalSpacer(Grid.d4)
                    TrackersSection(state.trackers, actions, Modifier.padding(horizontal = Grid.d3))
                }
                VerticalScrollbar(
                    adapter = rememberScrollbarAdapter(scrollState),
                    modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight()
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FavouritesSection(
    issues: ImmutableList<Issue>,
    actions: MainActions,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        Text(
            "⭐ Favourites",
            style = BpmTheme.typography.titleMedium,
            modifier = Modifier.fillMaxWidth()
        )
        VerticalSpacer(Grid.d2)
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(Grid.d1_5),
            verticalArrangement = Arrangement.spacedBy(Grid.d1_5)
        ) {
            for (issue in issues) {
                FavouriteIssueCard(
                    issue = issue,
                    onClick = { actions.onNewTracker(issue) }
                )
            }
        }
    }
}

@Composable
fun TrackersSection(
    trackers: ImmutableList<Tracker>,
    actions: MainActions,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        Text(
            "⏳ Trackers",
            style = BpmTheme.typography.titleMedium,
            modifier = Modifier.fillMaxWidth()
        )
        VerticalSpacer(Grid.d2)
        for (tracker in trackers) {
            TrackerRow(
                tracker = tracker,
                modifier = Modifier.fillMaxWidth(),
                onResume = { actions.onResumeTracker(tracker) },
                onPause = { actions.onPauseTracker(tracker) },
                onCommit = { actions.onCommitTracker(tracker) }
            )
        }
    }
}