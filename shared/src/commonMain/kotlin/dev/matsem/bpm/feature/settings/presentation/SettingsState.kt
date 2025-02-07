package dev.matsem.bpm.feature.settings.presentation

import dev.matsem.bpm.data.model.domain.User

sealed interface SettingsState {
    data class SignedOut(
        val jiraCloudName: String = "",
        val jiraEmail: String = "",
        val jiraApiToken: String = "",
        val tempoApiToken: String = "",
        val isLoading: Boolean = false,
    ) : SettingsState

    data class SignedIn(
        val user: User
    ) : SettingsState
}