package dev.matsem.bpm.data.persistence.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Credentials(

    @SerialName("jira_domain")
    val jiraDomain: String,

    @SerialName("email")
    val email: String,

    @SerialName("jira_token")
    val jiraApiToken: String,

    @SerialName("tempo_token")
    val tempoApiToken: String
)