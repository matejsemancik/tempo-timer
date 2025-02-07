package dev.matsem.bpm.data.model.domain

data class Credentials(
    val jiraCloudName: String,
    val email: String,
    val jiraApiToken: String,
    val tempoApiToken: String
) {
    val jiraApiHost: String
        get() = "https://${jiraCloudName}.atlassian.net/"
}