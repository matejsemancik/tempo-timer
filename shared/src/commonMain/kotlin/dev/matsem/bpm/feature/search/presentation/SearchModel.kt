package dev.matsem.bpm.feature.search.presentation

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.plus

internal class SearchModel() : SearchScreen {

    private val coroutineScope = CoroutineScope(Dispatchers.Main) + SupervisorJob()
    private val _state = MutableStateFlow(SearchState())
    override val state: StateFlow<SearchState> = _state
    override val actions: SearchActions = object : SearchActions {
        override fun onSearchInput(input: String) = _state.update { it.copy(input = input) }
    }
}