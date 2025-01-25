package dev.matsem.bpm.feature.settings.presentation

import kotlinx.coroutines.flow.StateFlow

interface SettingsScreen {
    val state: StateFlow<SettingsState>
    val actions: SettingsActions
}