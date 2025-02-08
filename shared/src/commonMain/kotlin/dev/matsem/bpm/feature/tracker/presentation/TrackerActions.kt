package dev.matsem.bpm.feature.tracker.presentation

import dev.matsem.bpm.data.repo.model.Issue
import dev.matsem.bpm.data.repo.model.Timer

interface TrackerActions {
    fun onNewTimer(issue: Issue)
    fun onResumeTimer(timer: Timer)
    fun onPauseTimer(timer: Timer)
    fun onDeleteTimer(timer: Timer)

    companion object {
        fun noOp() = object : TrackerActions {
            override fun onNewTimer(issue: Issue) = Unit
            override fun onResumeTimer(timer: Timer) = Unit
            override fun onPauseTimer(timer: Timer) = Unit
            override fun onDeleteTimer(timer: Timer) = Unit
        }
    }
}