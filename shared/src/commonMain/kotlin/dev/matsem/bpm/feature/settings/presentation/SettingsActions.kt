package dev.matsem.bpm.feature.settings.presentation

interface SettingsActions {
    fun onJiraCloudName(input: String)
    fun onJiraEmailInput(input: String)
    fun onJiraApiKeyInput(input: String)
    fun onTempoApiKeyInput(input: String)

    fun onLoginClick()
    fun onLogoutClick()
}