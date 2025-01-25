package dev.matsem.bpm.feature.tracker.presentation

import dev.matsem.bpm.feature.tracker.model.Tracker
import dev.matsem.bpm.feature.tracker.model.TrackerMock
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class MainState(
    val trackers: ImmutableList<Tracker> = TrackerMock.toImmutableList()
)

object MainViewModel {

    private val _state = MutableStateFlow(MainState())
    val state: StateFlow<MainState> = _state
}