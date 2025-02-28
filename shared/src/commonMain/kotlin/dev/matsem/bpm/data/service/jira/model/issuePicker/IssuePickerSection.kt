package dev.matsem.bpm.data.service.jira.model.issuePicker

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
    val issues: List<SuggestedIssue>,
)