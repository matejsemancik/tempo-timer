package dev.matsem.bpm.feature.settings.presentation

import dev.matsem.bpm.data.model.domain.Credentials
import dev.matsem.bpm.data.repo.SessionRepo
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

internal class SettingsModel(
    private val sessionRepo: SessionRepo,
) : SettingsScreen {

    companion object {
        private val DefaultState: SettingsState = SettingsState.SignedOut()
    }

    private val coroutineScope = CoroutineScope(Dispatchers.Main) + SupervisorJob()
    private val _state = MutableStateFlow<SettingsState>(DefaultState)
    override val state: StateFlow<SettingsState> = _state
        .onStart {
            // Load initial state
            coroutineScope.launch {
                val user = runCatching { sessionRepo.fetchUser() }.getOrNull()
                _state.update {
                    when (user) {
                        null -> SettingsState.SignedOut()
                        else -> SettingsState.SignedIn(user)
                    }
                }
            }
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
                sessionRepo
                    .signIn(
                        Credentials(
                            baseUrl = "https://${currentState.jiraHostname}.atlassian.net/rest/api/3/",
                            email = currentState.jiraEmail,
                            jiraApiToken = currentState.jiraApiToken,
                            tempoApiToken = currentState.tempoApiToken
                        )
                    )
            }
                .onSuccess { user -> _state.update { SettingsState.SignedIn(user) } }
                .onFailure {
                    println(it)
                    _state.update { currentState.copy(isLoading = false) }
                }
        }
    }

    private fun logout() {
        coroutineScope.launch {
            sessionRepo.signOut()
            _state.update { SettingsState.SignedOut() }
        }
    }
}