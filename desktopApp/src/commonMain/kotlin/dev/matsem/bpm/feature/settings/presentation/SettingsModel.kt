package dev.matsem.bpm.feature.settings.presentation

import dev.matsem.bpm.data.model.domain.Credentials
import dev.matsem.bpm.data.repo.CredentialsRepo
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

internal class SettingsModel(
    private val credentialsRepo: CredentialsRepo,
) : SettingsScreen {

    companion object {
        private val DefaultState: SettingsState = SettingsState.SignedOut()
    }

    private val coroutineScope = CoroutineScope(Dispatchers.Main) + SupervisorJob()
    private val _state = MutableStateFlow<SettingsState>(DefaultState)
    override val state: StateFlow<SettingsState> = _state
        .onStart {
            coroutineScope.launch {
                credentialsRepo.getUser().collect { user ->
                    _state.update {
                        when (user) {
                            null -> SettingsState.SignedOut()
                            else -> SettingsState.SignedIn(user)
                        }
                    }
                }
            }
            // Load initial state
        }
        .stateIn(coroutineScope, SharingStarted.Lazily, DefaultState)
    override val actions: SettingsActions = object : SettingsActions {

        override fun onJiraHostnameInput(input: String) = _state.update {
            when (it) {
                is SettingsState.SignedOut -> it.copy(jiraHostname = input)
                is SettingsState.SignedIn -> it
            }
        }

        override fun onJiraEmailInput(input: String) = _state.update {
            when (it) {
                is SettingsState.SignedOut -> it.copy(jiraEmail = input)
                is SettingsState.SignedIn -> it
            }
        }

        override fun onJiraApiKeyInput(input: String) = _state.update {
            when (it) {
                is SettingsState.SignedOut -> it.copy(jiraApiToken = input)
                is SettingsState.SignedIn -> it
            }
        }

        override fun onTempoApiKeyInput(input: String) = _state.update {
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
            val currentState = _state.value as? SettingsState.SignedOut ?: return@launch

            _state.update { currentState.copy(isLoading = true) }

            runCatching {
                credentialsRepo.signIn(
                    Credentials(
                        baseUrl = "https://${currentState.jiraHostname}.atlassian.net/rest/api/3/",
                        email = currentState.jiraEmail,
                        jiraApiToken = currentState.jiraApiToken,
                        tempoApiToken = currentState.tempoApiToken
                    )
                )
            }.onFailure {
                _state.update { currentState.copy(isLoading = false) }
            }
        }
    }

    private fun logout() {
        coroutineScope.launch {
            credentialsRepo.signOut()
        }
    }
}