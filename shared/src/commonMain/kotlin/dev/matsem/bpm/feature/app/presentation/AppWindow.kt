package dev.matsem.bpm.feature.app.presentation

import kotlinx.coroutines.flow.StateFlow

interface AppWindow {

    val state: StateFlow<AppWindowState>
    val actions: AppWindowActions
}