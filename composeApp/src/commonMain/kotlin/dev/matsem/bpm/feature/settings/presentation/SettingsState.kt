package dev.matsem.bpm.feature.settings.presentation

data class SettingsState(
    val jiraHostname: String = "",
    val jiraEmail: String = "",
    val jiraApiKey: String = "",
    val tempoApiKey: String = ""
)