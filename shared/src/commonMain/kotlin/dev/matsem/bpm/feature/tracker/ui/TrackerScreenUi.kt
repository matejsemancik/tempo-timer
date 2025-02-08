package dev.matsem.bpm.feature.tracker.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.matsem.bpm.data.model.domain.Issue
import dev.matsem.bpm.data.model.domain.MockIssues
import dev.matsem.bpm.data.model.domain.MockTimers
import dev.matsem.bpm.data.model.domain.Timer
import dev.matsem.bpm.design.theme.BpmTheme
import dev.matsem.bpm.design.theme.Grid
import dev.matsem.bpm.design.tooling.Showcase
import dev.matsem.bpm.design.tooling.VerticalSpacer
import dev.matsem.bpm.design.tooling.centeredVertically
import dev.matsem.bpm.feature.tracker.presentation.TrackerActions
import dev.matsem.bpm.feature.tracker.presentation.TrackerScreen
import dev.matsem.bpm.feature.tracker.presentation.TrackerState
import dev.matsem.bpm.feature.tracker.ui.widget.FavouriteIssueChip
import dev.matsem.bpm.feature.tracker.ui.widget.TimerRow
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject

@Composable
fun TrackerScreenUi(
    modifier: Modifier = Modifier,
    screen: TrackerScreen = koinInject(),
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
                VerticalSpacer(Grid.d3)
                AnimatedVisibility(state.favouriteIssues.isNotEmpty()) {
                    Column {
                        FavouritesSection(state.favouriteIssues, actions)
                        VerticalSpacer(Grid.d4)
                    }
                }
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
    Column(modifier.padding(horizontal = BpmTheme.dimensions.horizontalContentPadding)) {
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
    Column {
        Text(
            "⏳ Timers",
            style = BpmTheme.typography.titleMedium,
            modifier = Modifier.fillMaxWidth().padding(horizontal = BpmTheme.dimensions.horizontalContentPadding)
        )
        VerticalSpacer(Grid.d2)
        Crossfade(timers.isEmpty()) { isEmpty ->
            if (isEmpty) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = BpmTheme.dimensions.horizontalContentPadding)
                        .border(
                            width = Dp.Hairline,
                            color = BpmTheme.colorScheme.outline,
                            shape = BpmTheme.shapes.small
                        )
                        .background(
                            color = BpmTheme.colorScheme.surfaceContainer,
                            shape = BpmTheme.shapes.small
                        )
                        .padding(horizontal = Grid.d5, vertical = Grid.d4)
                ) {
                    Text(
                        text = "No running timers",
                        style = BpmTheme.typography.bodyLarge.centeredVertically(),
                        color = BpmTheme.colorScheme.outline,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    VerticalSpacer(Grid.d3)
                    Text(
                        text = "Start a new timer from menu bar down there 👇",
                        style = BpmTheme.typography.bodyMedium.centeredVertically(),
                        color = BpmTheme.colorScheme.outline,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            } else {
                Column {
                    for (timer in timers) {
                        TimerRow(
                            tracker = timer,
                            modifier = Modifier.fillMaxWidth(),
                            contentPadding = PaddingValues(horizontal = BpmTheme.dimensions.horizontalContentPadding),
                            onResume = { actions.onResumeTimer(timer) },
                            onPause = { actions.onPauseTimer(timer) },
                            onDelete = { actions.onDeleteTimer(timer) },
                            onOpenDetail = {
                                println("onOpenDetail: ${timer.issue?.key}")
                            },
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun TrackerScreenPreview_Empty() {
    Showcase {
        TrackerScreenUi(
            state = TrackerState(
                favouriteIssues = persistentListOf(),
                timers = persistentListOf()
            ),
            actions = TrackerActions.noOp()
        )
    }
}

@Preview
@Composable
fun TrackerScreenPreview_Favourites() {
    Showcase {
        TrackerScreenUi(
            state = TrackerState(
                favouriteIssues = MockIssues.toPersistentList(),
                timers = persistentListOf()
            ),
            actions = TrackerActions.noOp()
        )
    }
}

@Preview
@Composable
fun TrackerScreenPreview_Favourites_Timers() {
    Showcase {
        TrackerScreenUi(
            state = TrackerState(
                favouriteIssues = MockIssues.toPersistentList(),
                timers = MockTimers.toPersistentList()
            ),
            actions = TrackerActions.noOp()
        )
    }
}