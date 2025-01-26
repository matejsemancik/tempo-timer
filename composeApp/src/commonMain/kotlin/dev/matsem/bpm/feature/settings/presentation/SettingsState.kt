package dev.matsem.bpm.feature.settings.presentation

data class SettingsState(
    val jiraUrl: String = "",
    val jiraEmail: String = "",
    val jiraApiKey: String = "",
    val tempoApiKey: String = ""
)