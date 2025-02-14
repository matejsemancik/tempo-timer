package dev.matsem.bpm.feature.tracker.presentation

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface TrackerScreen {
    val state: StateFlow<TrackerState>
    val actions: TrackerActions
    val events: Flow<TrackerEvent>
}