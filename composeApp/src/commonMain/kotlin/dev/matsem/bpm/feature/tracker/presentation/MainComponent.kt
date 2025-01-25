package dev.matsem.bpm.feature.tracker.presentation

import dev.matsem.bpm.feature.tracker.model.Tracker
import dev.matsem.bpm.feature.tracker.model.TrackerLog
import dev.matsem.bpm.feature.tracker.model.TrackerMock
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.datetime.Clock

interface MainActions {
    fun onResumeTracker(tracker: Tracker)
    fun onPauseTracker(tracker: Tracker)
    fun onCommitTracker(tracker: Tracker)
}

data class MainState(
    val trackers: PersistentList<Tracker> = TrackerMock.toPersistentList()
)

object MainComponent : MainActions {

    private val _state = MutableStateFlow(MainState())
    val state: StateFlow<MainState> = _state
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
        val newLogs = tracker.state.finishedLogs + TrackerLog(start = tracker.state.startedAt, end = Clock.System.now())
        state.copy(
            trackers = state.trackers.set(
                idx,
                tracker.copy(state = tracker.state.copy(startedAt = null, finishedLogs = newLogs))
            )
        )
    }

    override fun onCommitTracker(tracker: Tracker) {
        println("Not yet implemented")
    }
}