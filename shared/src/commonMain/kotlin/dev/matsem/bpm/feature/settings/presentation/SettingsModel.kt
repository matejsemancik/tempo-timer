package dev.matsem.bpm.feature.settings.presentation

import androidx.compose.ui.text.input.TextFieldValue
import dev.matsem.bpm.arch.BaseModel
import dev.matsem.bpm.data.repo.IssueRepo
import dev.matsem.bpm.data.repo.PreferenceRepo
import dev.matsem.bpm.data.repo.SessionRepo
import dev.matsem.bpm.data.repo.model.AppThemeMode
import dev.matsem.bpm.data.repo.model.Credentials
import dev.matsem.bpm.data.repo.model.User
import kotlinx.coroutines.launch

internal class SettingsModel(
    private val sessionRepo: SessionRepo,
    private val issueRepo: IssueRepo,
    private val preferenceRepo: PreferenceRepo,
) : BaseModel<SettingsState, Nothing>(DefaultState), SettingsScreen {

    companion object {
        private val DefaultState: SettingsState = SettingsState(
            accountState = SettingsState.AccountState.SignedIn(
                user = User("", "", "", "")
            ),
            themeMode = AppThemeMode.SYSTEM
        )
    }

    override suspend fun onStart() {
        coroutineScope.launch {
            sessionRepo.getUser().collect { user ->
                updateState { state ->
                    val accountState = when (user) {
                        null -> SettingsState.AccountState.SignedOut()
                        else -> SettingsState.AccountState.SignedIn(user)
                    }

                    state.copy(accountState = accountState)
                }
            }
        }
        coroutineScope.launch {
            preferenceRepo.observeAppThemeMode().collect { mode ->
                updateState { state ->
                    state.copy(themeMode = mode)
                }
            }
        }
    }

    override val actions: SettingsActions = object : SettingsActions {
        override fun onAppThemeModeClick(mode: AppThemeMode) {
            coroutineScope.launch {
                preferenceRepo.saveAppThemeMode(mode)
            }
        }

        override fun onJiraCloudName(input: TextFieldValue) = updateState { state ->
            val accountState = when (val accountState = state.accountState) {
                is SettingsState.AccountState.SignedOut -> accountState.copy(jiraCloudName = input)
                is SettingsState.AccountState.SignedIn -> accountState
            }
            state.copy(accountState = accountState)
        }

        override fun onJiraEmailInput(input: TextFieldValue) = updateState { state ->
            val accountState = when (val accountState = state.accountState) {
                is SettingsState.AccountState.SignedOut -> accountState.copy(jiraEmail = input)
                is SettingsState.AccountState.SignedIn -> accountState
            }
            state.copy(accountState = accountState)
        }

        override fun onJiraApiKeyInput(input: TextFieldValue) = updateState { state ->
            val accountState = when (val accountState = state.accountState) {
                is SettingsState.AccountState.SignedOut -> accountState.copy(jiraApiToken = input)
                is SettingsState.AccountState.SignedIn -> accountState
            }
            state.copy(accountState = accountState)
        }

        override fun onTempoApiKeyInput(input: TextFieldValue) = updateState { state ->
            val accountState = when (val accountState = state.accountState) {
                is SettingsState.AccountState.SignedOut -> accountState.copy(tempoApiToken = input)
                is SettingsState.AccountState.SignedIn -> accountState
            }
            state.copy(accountState = accountState)
        }

        override fun onLoginClick() = login()

        override fun onLogoutClick() = logout()
    }

    private fun login() {
        coroutineScope.launch {
            val accountState = state.value.accountState as? SettingsState.AccountState.SignedOut ?: return@launch
            updateState { state -> state.copy(accountState = accountState.copy(isLoading = true)) }
            runCatching {
                sessionRepo
                    .signIn(
                        Credentials(
                            jiraDomain = accountState.jiraCloudName.text.trim(),
                            email = accountState.jiraEmail.text.trim(),
                            jiraApiToken = accountState.jiraApiToken.text.trim(),
                            tempoApiToken = accountState.tempoApiToken.text.trim()
                        )
                    )
            }.onFailure {
                updateState { state -> state.copy(accountState = accountState.copy(isLoading = false)) }
                println(it)
            }
        }
    }

    private fun logout() {
        coroutineScope.launch {
            issueRepo.deleteAllIssues()
            sessionRepo.signOut()
        }
    }
}
