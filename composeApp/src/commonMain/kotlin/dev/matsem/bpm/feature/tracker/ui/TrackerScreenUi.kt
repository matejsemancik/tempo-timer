package dev.matsem.bpm.feature.tracker.ui

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.matsem.bpm.design.theme.BpmTheme
import dev.matsem.bpm.design.theme.Grid
import dev.matsem.bpm.design.tooling.VerticalSpacer
import dev.matsem.bpm.feature.tracker.model.Issue
import dev.matsem.bpm.feature.tracker.model.Timer
import dev.matsem.bpm.feature.tracker.presentation.TrackerActions
import dev.matsem.bpm.feature.tracker.presentation.TrackerScreen
import dev.matsem.bpm.feature.tracker.presentation.TrackerState
import dev.matsem.bpm.feature.tracker.ui.widget.FavouriteIssueChip
import dev.matsem.bpm.feature.tracker.ui.widget.TimerRow
import kotlinx.collections.immutable.ImmutableList

@Composable
fun TrackerScreenUi(
    screen: TrackerScreen,
    modifier: Modifier = Modifier
) {
    val state by screen.state.collectAsStateWithLifecycle()
    TrackerScreenUi(
        state = state,
        actions = screen.actions,
        modifier = modifier
    )
}

@Composable
fun TrackerScreenUi(
    state: TrackerState,
    actions: TrackerActions,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        color = BpmTheme.colorScheme.background
    ) {
        val scrollState = rememberScrollState()
        Box {
            Column(modifier = Modifier.verticalScroll(scrollState)) {
                VerticalSpacer(Grid.d2)
                FavouritesSection(state.favouriteIssues, actions)
                VerticalSpacer(Grid.d4)
                TrackersSection(state.timers, actions)
            }
            VerticalScrollbar(
                adapter = rememberScrollbarAdapter(scrollState),
                modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight()
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FavouritesSection(
    issues: ImmutableList<Issue>,
    actions: TrackerActions,
    modifier: Modifier = Modifier
) {
    Column(modifier.padding(horizontal = Grid.d3)) {
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
                FavouriteIssueChip(
                    issue = issue,
                    onClick = { actions.onNewTimer(issue) }
                )
            }
        }
    }
}

@Composable
fun TrackersSection(
    timers: ImmutableList<Timer>,
    actions: TrackerActions,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        Text(
            "⏳ Timers",
            style = BpmTheme.typography.titleMedium,
            modifier = Modifier.fillMaxWidth().padding(horizontal = Grid.d3)
        )
        VerticalSpacer(Grid.d2)
        for (tracker in timers) {
            TimerRow(
                tracker = tracker,
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = Grid.d3),
                onResume = { actions.onResumeTimer(tracker) },
                onPause = { actions.onPauseTimer(tracker) },
                onDelete = { actions.onDeleteTimer(tracker) },
                onOpenDetail = {
                    println("onOpenDetail: ${tracker.issue?.key}")
                },
            )
        }
    }
}