package dev.matsem.bpm.feature.tracker.presentation

import kotlinx.coroutines.flow.StateFlow

interface TrackerScreen {
    val state: StateFlow<TrackerState>
    val actions: TrackerActions
}