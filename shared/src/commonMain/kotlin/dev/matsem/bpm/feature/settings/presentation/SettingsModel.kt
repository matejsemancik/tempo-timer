package dev.matsem.bpm.feature.settings.presentation

import dev.matsem.bpm.data.repo.model.Credentials
import dev.matsem.bpm.data.repo.model.User
import dev.matsem.bpm.data.repo.SessionRepo
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

internal class SettingsModel(
    private val sessionRepo: SessionRepo,
) : SettingsScreen {

    companion object {
        private val DefaultState: SettingsState = SettingsState.SignedIn(User("", "", ""))
    }

    private val coroutineScope = CoroutineScope(Dispatchers.Main) + SupervisorJob()
    private val _state = MutableStateFlow(DefaultState)
    override val state: StateFlow<SettingsState> = _state
        .onStart {
            coroutineScope.launch {
                sessionRepo.getUser().collect { user ->
                    _state.update {
                        when (user) {
                            null -> SettingsState.SignedOut()
                            else -> SettingsState.SignedIn(user)
                        }
                    }
                }
            }
        }
        .stateIn(coroutineScope, SharingStarted.Lazily, DefaultState)
    override val actions: SettingsActions = object : SettingsActions {

        override fun onJiraCloudName(input: String) = _state.update {
            when (it) {
                is SettingsState.SignedOut -> it.copy(jiraCloudName = input)
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
                            jiraCloudName = currentState.jiraCloudName.trim(),
                            email = currentState.jiraEmail.trim(),
                            jiraApiToken = currentState.jiraApiToken.trim(),
                            tempoApiToken = currentState.tempoApiToken.trim()
                        )
                    )
            }.onFailure {
                _state.update { currentState.copy(isLoading = false) }
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