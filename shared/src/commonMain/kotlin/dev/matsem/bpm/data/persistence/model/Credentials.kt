package dev.matsem.bpm.data.persistence.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Credentials(

    @SerialName("jira_cloud_name")
    val jiraCloudName: String,

    @SerialName("email")
    val email: String,

    @SerialName("jira_token")
    val jiraApiToken: String,

    @SerialName("tempo_token")
    val tempoApiToken: String
)