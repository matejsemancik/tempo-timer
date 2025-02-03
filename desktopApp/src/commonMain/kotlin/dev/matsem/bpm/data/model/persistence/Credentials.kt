package dev.matsem.bpm.data.model.persistence

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Credentials(

    @SerialName("base_url")
    val baseUrl: String,

    @SerialName("email")
    val email: String,

    @SerialName("jira_token")
    val jiraApiToken: String,

    @SerialName("tempo_token")
    val tempoApiToken: String
)