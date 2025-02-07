package dev.matsem.bpm.data.model.network.jira.issuePicker

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SuggestedIssue(

    @SerialName("id")
    val id: Long,

    @SerialName("img")
    val iconUrlPath: String,

    @SerialName("key")
    val key: String,

    @SerialName("summaryText")
    val summary: String
)