package dev.matsem.bpm.data.model.network.jira.issuePicker

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class IssuePickerSection(

    @SerialName("id")
    val id: String,

    @SerialName("label")
    val label: String,

    @SerialName("sub")
    val sub: String? = null,

    @SerialName("issues")
    val issues: List<SuggestedIssue>
)