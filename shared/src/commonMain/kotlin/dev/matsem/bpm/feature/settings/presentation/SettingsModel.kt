package dev.matsem.bpm.feature.settings.presentation

import androidx.compose.ui.text.input.TextFieldValue
import dev.matsem.bpm.arch.BaseModel
import dev.matsem.bpm.data.repo.SessionRepo
import dev.matsem.bpm.data.repo.model.Credentials
import dev.matsem.bpm.data.repo.model.User
import kotlinx.coroutines.launch

internal class SettingsModel(
    private val sessionRepo: SessionRepo,
) : BaseModel<SettingsState, Nothing>(DefaultState), SettingsScreen {

    companion object {
        private val DefaultState: SettingsState = SettingsState.SignedIn(User("", "", ""))
    }

    override suspend fun onStart() {
        coroutineScope.launch {
            sessionRepo.getUser().collect { user ->
                updateState {
                    when (user) {
                        null -> SettingsState.SignedOut()
                        else -> SettingsState.SignedIn(user)
                    }
                }
            }
        }
    }
    override val actions: SettingsActions = object : SettingsActions {

        override fun onJiraCloudName(input: TextFieldValue) = updateState {
            when (it) {
                is SettingsState.SignedOut -> it.copy(jiraCloudName = input)
                is SettingsState.SignedIn -> it
            }
        }

        override fun onJiraEmailInput(input: TextFieldValue) = updateState {
            when (it) {
                is SettingsState.SignedOut -> it.copy(jiraEmail = input)
                is SettingsState.SignedIn -> it
            }
        }

        override fun onJiraApiKeyInput(input: TextFieldValue) = updateState {
            when (it) {
                is SettingsState.SignedOut -> it.copy(jiraApiToken = input)
                is SettingsState.SignedIn -> it
            }
        }

        override fun onTempoApiKeyInput(input: TextFieldValue) = updateState {
            when (it) {
                is SettingsState.SignedIn -> it
                is SettingsState.SignedOut -> it.copy(tempoApiToken = input)
            }
        }

        override fun onLoginClick() = login()

        override fun onLogoutClick() = logout()
    }

    private fun login() {
        coroutineScope.launch {
            val currentState = state.value as? SettingsState.SignedOut ?: return@launch
            updateState { currentState.copy(isLoading = true) }
            runCatching {
                sessionRepo
                    .signIn(
                        Credentials(
                            jiraCloudName = currentState.jiraCloudName.text.trim(),
                            email = currentState.jiraEmail.text.trim(),
                            jiraApiToken = currentState.jiraApiToken.text.trim(),
                            tempoApiToken = currentState.tempoApiToken.text.trim()
                        )
                    )
            }.onFailure {
                updateState { currentState.copy(isLoading = false) }
                println(it)
            }
        }
    }

    private fun logout() {
        coroutineScope.launch {
            sessionRepo.signOut()
        }
    }
}