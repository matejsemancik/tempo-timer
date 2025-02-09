package dev.matsem.bpm.feature.tracker.presentation

import dev.matsem.bpm.data.repo.IssueRepo
import dev.matsem.bpm.data.repo.model.Issue
import dev.matsem.bpm.data.repo.model.Timer
import dev.matsem.bpm.data.repo.model.TimerState
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.datetime.Clock

internal class TrackerModel(
    private val issueRepo: IssueRepo,
) : TrackerScreen {

    companion object {
        private val DefaultState = TrackerState()
    }

    private val coroutineScope = CoroutineScope(Dispatchers.Main) + SupervisorJob()
    private val _state = MutableStateFlow(DefaultState)
    override val state: StateFlow<TrackerState> = _state.onStart {
        coroutineScope.launch {
            issueRepo.getFavouriteIssues().collect { favs ->
                _state.update { it.copy(favouriteIssues = favs.toImmutableList()) }
            }
        }
    }.stateIn(
        scope = coroutineScope,
        started = SharingStarted.Lazily,
        initialValue = DefaultState
    )
    override val actions: TrackerActions = object : TrackerActions {
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

        override fun onDeleteTimer(timer: Timer) = _state.update { state ->
            state.copy(timers = state.timers.remove(timer))
        }
    }
}