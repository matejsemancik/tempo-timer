package dev.matsem.bpm.data.repo.model

data class Credentials(
    val jiraCloudName: String,
    val email: String,
    val jiraApiToken: String,
    val tempoApiToken: String
) {
    val jiraApiHost: String
        get() = "https://${jiraCloudName}.atlassian.net/"
}