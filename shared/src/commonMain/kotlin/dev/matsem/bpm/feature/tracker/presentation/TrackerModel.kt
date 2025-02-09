package dev.matsem.bpm.feature.tracker.presentation

import dev.matsem.bpm.data.repo.IssueRepo
import dev.matsem.bpm.data.repo.TimerRepo
import dev.matsem.bpm.data.repo.model.Issue
import dev.matsem.bpm.data.repo.model.Timer
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

internal class TrackerModel(
    private val issueRepo: IssueRepo,
    private val timerRepo: TimerRepo,
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

        coroutineScope.launch {
            timerRepo.getTimers().collect { timers ->
                _state.update { it.copy(timers = timers.toImmutableList()) }
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

            coroutineScope.launch {
                timerRepo.createTimerForIssue(issue)
            }
        }

        override fun onResumeTimer(timer: Timer) {
            coroutineScope.launch {
                timerRepo.resumeTimer(timer.id)
            }
        }

        override fun onPauseTimer(timer: Timer) {
            coroutineScope.launch {
                timerRepo.pauseTimer(timer.id)
            }
        }

        override fun onDeleteTimer(timer: Timer) {
            coroutineScope.launch {
                timerRepo.deleteTimer(timer.id)
            }
        }

        override fun onDeleteFavourite(issue: Issue) {
            coroutineScope.launch {
                issueRepo.removeFavouriteIssue(issue)
            }
        }
    }
}