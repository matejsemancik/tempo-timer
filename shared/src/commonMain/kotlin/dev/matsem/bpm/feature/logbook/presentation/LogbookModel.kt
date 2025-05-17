package dev.matsem.bpm.feature.logbook.presentation

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class LogbookModel : LogbookScreen {
    private val _state = MutableStateFlow(LogbookState())
    override val state: StateFlow<LogbookState> = _state.asStateFlow()
    
    override val actions: LogbookActions = object : LogbookActions {
        // Implement action methods as needed
    }
}
