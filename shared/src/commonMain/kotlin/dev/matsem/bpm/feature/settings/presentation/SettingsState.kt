package dev.matsem.bpm.feature.settings.presentation

import androidx.compose.ui.text.input.TextFieldValue
import dev.matsem.bpm.data.repo.model.User

sealed interface SettingsState {
    data class SignedOut(
        val jiraCloudName: TextFieldValue = TextFieldValue(),
        val jiraEmail: TextFieldValue = TextFieldValue(),
        val jiraApiToken: TextFieldValue = TextFieldValue(),
        val tempoApiToken: TextFieldValue = TextFieldValue(),
        val isLoading: Boolean = false,
    ) : SettingsState

    data class SignedIn(
        val user: User,
    ) : SettingsState
}