package dev.matsem.bpm.feature.tracker.presentation

import dev.matsem.bpm.data.model.domain.Issue
import dev.matsem.bpm.data.model.domain.Timer

interface TrackerActions {
    fun onNewTimer(issue: Issue)
    fun onResumeTimer(timer: Timer)
    fun onPauseTimer(timer: Timer)
    fun onDeleteTimer(timer: Timer)
}