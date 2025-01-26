package dev.matsem.bpm.feature.settings.presentation

interface SettingsActions {
    fun onJiraUrlInput(input: String)
    fun onJiraEmailInput(input: String)
    fun onJiraApiKeyInput(input: String)
    fun onTempoApiKeyInput(input: String)
}