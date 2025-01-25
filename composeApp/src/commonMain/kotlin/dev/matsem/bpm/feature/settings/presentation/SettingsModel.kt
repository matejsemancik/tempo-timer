package dev.matsem.bpm.feature.settings.presentation

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

object SettingsModel : SettingsScreen {
    private val _state = MutableStateFlow(SettingsState())
    override val state: StateFlow<SettingsState> = _state
    override val actions: SettingsActions = object : SettingsActions {
        override fun onJiraApiKeyInput(input: String) = _state.update {
            it.copy(jiraApiKey = input)
        }

        override fun onTempoApiKeyInput(input: String) = _state.update {
            it.copy(tempoApiKey = input)
        }
    }
}