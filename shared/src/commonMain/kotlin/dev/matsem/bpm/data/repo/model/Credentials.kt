package dev.matsem.bpm.data.repo.model

import dev.matsem.bpm.tooling.Constants

data class Credentials(
    val jiraDomain: String,
    val email: String,
    val jiraApiToken: String,
    val tempoApiToken: String
) {
    val jiraApiUrl: String
        get() = Constants.JiraApiUrl(jiraDomain)
}