package dev.matsem.bpm.data.model.domain

data class Credentials(
    val baseUrl: String,
    val email: String,
    val jiraApiKey: String,
    val tempoApiKey: String
)