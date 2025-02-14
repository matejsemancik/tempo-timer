package dev.matsem.bpm.feature.tracker.presentation

import dev.matsem.bpm.arch.BaseModel
import dev.matsem.bpm.data.repo.IssueRepo
import dev.matsem.bpm.data.repo.TimerRepo
import dev.matsem.bpm.data.repo.model.Issue
import dev.matsem.bpm.data.repo.model.Timer
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch

internal class TrackerModel(
    private val issueRepo: IssueRepo,
    private val timerRepo: TimerRepo,
) : BaseModel<TrackerState, TrackerEvent>(TrackerState()), TrackerScreen {

    override suspend fun onStart() {
        coroutineScope.launch {
            issueRepo.getFavouriteIssues().collect { favs ->
                updateState { it.copy(favouriteIssues = favs.toImmutableList()) }
            }
        }

        coroutineScope.launch {
            timerRepo.getTimers().collect { timers ->
                updateState { it.copy(timers = timers.toImmutableList()) }
            }
        }
    }

    override val actions: TrackerActions = object : TrackerActions {
        override fun onNewTimer(issue: Issue) {
            if (state.value.timers.any { it.issue == issue }) {
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

        override fun onCommitTimer(timer: Timer) {
            sendEvent(TrackerEvent.OpenCommitDialog(timer = timer))
        }
    }
}