package dev.matsem.bpm.feature.logbook.presentation

import kotlinx.coroutines.flow.StateFlow

interface LogbookScreen {
    val state: StateFlow<LogbookState>
    val actions: LogbookActions
}
