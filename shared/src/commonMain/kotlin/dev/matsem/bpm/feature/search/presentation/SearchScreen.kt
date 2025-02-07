package dev.matsem.bpm.feature.search.presentation

import kotlinx.coroutines.flow.StateFlow

interface SearchScreen {
    val state: StateFlow<SearchState>
    val actions: SearchActions
}