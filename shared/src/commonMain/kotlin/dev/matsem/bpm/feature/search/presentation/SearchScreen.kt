package dev.matsem.bpm.feature.search.presentation

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface SearchScreen {
    val state: StateFlow<SearchState>
    val actions: SearchActions
    val events: Flow<SearchEvent>
}