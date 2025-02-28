package dev.matsem.bpm.data.service.jira.model.issuePicker

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class IssuePickerResponse(

    @SerialName("sections")
    val sections: List<IssuePickerSection>,
)