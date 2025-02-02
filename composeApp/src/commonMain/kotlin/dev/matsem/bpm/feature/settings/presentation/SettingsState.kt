package dev.matsem.bpm.feature.settings.presentation

import dev.matsem.bpm.data.model.domain.User

sealed interface SettingsState {
    data class SignedOut(
        val jiraHostname: String = "",
        val jiraEmail: String = "",
        val jiraApiToken: String = "",
        val tempoApiToken: String = ""
    ) : SettingsState

    data class SignedIn(
        val user: User
    ) : SettingsState
}