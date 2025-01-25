package dev.matsem.bpm.feature.tracker.presentation

import dev.matsem.bpm.feature.tracker.model.Issue
import dev.matsem.bpm.feature.tracker.model.Timer

interface TrackerActions {
    fun onNewTimer(issue: Issue)
    fun onResumeTimer(timer: Timer)
    fun onPauseTimer(timer: Timer)
    fun onDeleteTimer(timer: Timer)
}