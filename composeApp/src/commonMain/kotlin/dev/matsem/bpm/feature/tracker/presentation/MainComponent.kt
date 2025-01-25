package dev.matsem.bpm.feature.tracker.presentation

import dev.matsem.bpm.feature.tracker.model.Issue
import dev.matsem.bpm.feature.tracker.model.Tracker
import dev.matsem.bpm.feature.tracker.model.TrackerMock
import dev.matsem.bpm.feature.tracker.model.TrackerState
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.datetime.Clock

interface MainActions {
    fun onNewTracker(issue: Issue)
    fun onResumeTracker(tracker: Tracker)
    fun onPauseTracker(tracker: Tracker)
    fun onCommitTracker(tracker: Tracker)
}

data class MainState(
    val favouriteIssues: PersistentList<Issue> = TrackerMock.mapNotNull { it.issue }.toPersistentList(),
    val trackers: PersistentList<Tracker> = persistentListOf()
)

object MainComponent : MainActions {

    private val _state = MutableStateFlow(MainState())
    val state: StateFlow<MainState> = _state

    override fun onNewTracker(issue: Issue) {
        if (_state.value.trackers.any { it.issue == issue }) {
            return
        }

        _state.update { state ->
            state.copy(
                trackers = state.trackers.add(
                    Tracker(
                        issue = issue,
                        state = TrackerState(startedAt = Clock.System.now())
                    )
                )
            )
        }
    }

    override fun onResumeTracker(tracker: Tracker) = _state.update { state ->
        println("onResumeTracker ${tracker.issue?.key}")
        val idx = state.trackers.indexOf(tracker).takeIf { it >= 0 } ?: return@update state
        state.copy(
            trackers = state.trackers.set(
                idx,
                tracker.copy(state = tracker.state.copy(startedAt = Clock.System.now()))
            )
        )
    }

    override fun onPauseTracker(tracker: Tracker) = _state.update { state ->
        println("onPauseTracker ${tracker.issue?.key}")
        val idx = state.trackers.indexOf(tracker).takeIf { it >= 0 } ?: return@update state
        if (tracker.state.startedAt == null) {
            return@update state
        }

        val durationToAdd = Clock.System.now() - tracker.state.startedAt
        state.copy(
            trackers = state.trackers.set(
                idx,
                tracker.copy(
                    state = tracker.state.copy(
                        startedAt = null,
                        finishedDuration = tracker.state.finishedDuration + durationToAdd
                    )
                )
            )
        )
    }

    override fun onCommitTracker(tracker: Tracker) {
        println("Not yet implemented")
    }
}