package dev.matsem.bpm.feature.settings.presentation

import androidx.compose.ui.text.input.TextFieldValue
import dev.matsem.bpm.data.repo.model.AppThemeMode
import dev.matsem.bpm.data.repo.model.User
import io.ktor.websocket.FrameParser

data class SettingsState(
    val accountState: AccountState,
    val themeMode: AppThemeMode,
) {
    sealed interface AccountState {

        data class SignedOut(
            val jiraCloudName: TextFieldValue = TextFieldValue(),
            val jiraEmail: TextFieldValue = TextFieldValue(),
            val jiraApiToken: TextFieldValue = TextFieldValue(),
            val tempoApiToken: TextFieldValue = TextFieldValue(),
            val isLoading: Boolean = false,
        ) : AccountState

        data class SignedIn(
            val user: User,
        ) : AccountState
    }
}
