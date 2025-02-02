package dev.matsem.bpm.feature.settings.presentation

import dev.matsem.bpm.data.model.domain.Credentials
import dev.matsem.bpm.data.repo.CredentialsRepo
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

internal class SettingsModel(private val credentialsRepo: CredentialsRepo) : SettingsScreen {

    private val coroutineScope = CoroutineScope(Dispatchers.Main) + SupervisorJob()
    private val _state = MutableStateFlow(SettingsState())
    override val state: StateFlow<SettingsState> = _state
    override val actions: SettingsActions = object : SettingsActions {
        override fun onJiraHostnameInput(input: String) = _state.update {
            it.copy(jiraHostname = input)
        }

        override fun onJiraEmailInput(input: String) = _state.update {
            it.copy(jiraEmail = input)
        }

        override fun onJiraApiKeyInput(input: String) = _state.update {
            it.copy(jiraApiKey = input)
        }

        override fun onTempoApiKeyInput(input: String) = _state.update {
            it.copy(tempoApiKey = input)
        }

        override fun onLoginClick() = login()
    }

    private fun login() {
        coroutineScope.launch {
            val state = _state.value
            credentialsRepo.signIn(
                Credentials(
                    baseUrl = "https://${state.jiraHostname}.atlassian.net/rest/api/3/",
                    email = state.jiraEmail,
                    jiraApiToken = state.jiraApiKey,
                    tempoApiToken = state.tempoApiKey
                )
            )
        }
    }
}