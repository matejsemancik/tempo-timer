package dev.matsem.bpm

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.LightMode
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.Group
import dev.matsem.bpm.design.theme.BpmTheme
import dev.matsem.bpm.design.theme.Grid
import dev.matsem.bpm.design.tooling.VerticalSpacer
import dev.matsem.bpm.feature.tracker.model.Issue
import dev.matsem.bpm.feature.tracker.model.Tracker
import dev.matsem.bpm.feature.tracker.presentation.MainActions
import dev.matsem.bpm.feature.tracker.presentation.MainComponent
import dev.matsem.bpm.feature.tracker.ui.FavouriteIssueChip
import dev.matsem.bpm.feature.tracker.ui.TrackerRow
import kotlinx.collections.immutable.ImmutableList
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    val state by MainComponent.state.collectAsState()
    val actions = MainComponent
    val isSystemInDarkTheme = isSystemInDarkTheme() // Stores initial state of dark mode and stores in [darkMode] state.
    var darkMode by remember { mutableStateOf(isSystemInDarkTheme) }

    BpmTheme(isDark = darkMode) {
        Scaffold(
            bottomBar = {
                BottomAppBar(
                    actions = {
                        IconButton(onClick = { /* do something */ }) {
                            Icon(Icons.Rounded.Search, contentDescription = "Search issues")
                        }
                        IconButton(onClick = {
                            darkMode = !darkMode
                        }) {
                            Icon(
                                if (darkMode) Icons.Rounded.LightMode else Icons.Rounded.DarkMode,
                                contentDescription = "Toggle dark mode",
                            )
                        }
                        IconButton(onClick = { /* do something */ }) {
                            Icon(
                                Icons.Filled.Settings,
                                contentDescription = "Settings",
                            )
                        }
                        Text(
                            "tempo-desktop 0.0.1",
                            style = BpmTheme.typography.labelMedium,
                            modifier = Modifier.fillMaxWidth().padding(horizontal = Grid.d3)
                        )
                    },
                    floatingActionButton = {
                        ExtendedFloatingActionButton(
                            onClick = { /* do something */ },
                            containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                            elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
                        ) {
                            Icon(Icons.Rounded.Add, "Add tracker")
                            Text("Timer")
                        }
                    }
                )
            }
        ) { contentPadding ->
            Surface(
                modifier = Modifier.fillMaxSize().padding(contentPadding),
                color = BpmTheme.colorScheme.background
            ) {
                val scrollState = rememberScrollState()
                Box {
                    Column(modifier = Modifier.verticalScroll(scrollState)) {
                        VerticalSpacer(Grid.d2)
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
                FavouriteIssueChip(
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
            "⏳ Timers",
            style = BpmTheme.typography.titleMedium,
            modifier = Modifier.fillMaxWidth()
        )
        VerticalSpacer(Grid.d2)
        Column {
            for (tracker in trackers) {
                TrackerRow(
                    tracker = tracker,
                    modifier = Modifier.fillMaxWidth(),
                    onResume = { actions.onResumeTracker(tracker) },
                    onPause = { actions.onPauseTracker(tracker) },
                    onDelete = { actions.onDeleteTracker(tracker) },
                    onOpenDetail = {
                        println("onOpenDetail: ${tracker.issue?.key}")
                    },
                )
            }
        }
        Column {

        }
    }
}