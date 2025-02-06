package dev.matsem.bpm.data.model.domain

data class Credentials(
    val baseUrl: String,
    val email: String,
    val jiraApiToken: String,
    val tempoApiToken: String
)