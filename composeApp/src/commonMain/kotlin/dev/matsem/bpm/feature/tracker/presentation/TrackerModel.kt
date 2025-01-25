package dev.matsem.bpm.feature.tracker.presentation

import dev.matsem.bpm.feature.tracker.model.Issue
import dev.matsem.bpm.feature.tracker.model.Timer
import dev.matsem.bpm.feature.tracker.model.TimerState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.datetime.Clock

object TrackerModel : TrackerActions {

    private val _state = MutableStateFlow(TrackerState())
    val state: StateFlow<TrackerState> = _state

    override fun onNewTimer(issue: Issue) {
        if (_state.value.timers.any { it.issue == issue }) {
            return
        }

        _state.update { state ->
            state.copy(
                timers = state.timers.add(
                    Timer(
                        issue = issue,
                        state = TimerState(startedAt = Clock.System.now())
                    )
                )
            )
        }
    }

    override fun onResumeTimer(timer: Timer) = _state.update { state ->
        val idx = state.timers.indexOf(timer).takeIf { it >= 0 } ?: return@update state
        state.copy(
            timers = state.timers.set(
                idx,
                timer.copy(state = timer.state.copy(startedAt = Clock.System.now()))
            )
        )
    }

    override fun onPauseTimer(timer: Timer) = _state.update { state ->
        val idx = state.timers.indexOf(timer).takeIf { it >= 0 } ?: return@update state
        if (timer.state.startedAt == null) {
            return@update state
        }

        val durationToAdd = Clock.System.now() - timer.state.startedAt
        state.copy(
            timers = state.timers.set(
                idx,
                timer.copy(
                    state = timer.state.copy(
                        startedAt = null,
                        finishedDuration = timer.state.finishedDuration + durationToAdd
                    )
                )
            )
        )
    }

    override fun onDeleteTimer(timer: Timer) = TrackerModel._state.update { state ->
        state.copy(timers = state.timers.remove(timer))
    }
}