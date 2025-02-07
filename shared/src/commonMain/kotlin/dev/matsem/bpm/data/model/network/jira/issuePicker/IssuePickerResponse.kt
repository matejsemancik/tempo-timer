package dev.matsem.bpm.data.model.network.jira.issuePicker

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class IssuePickerResponse(

    @SerialName("sections")
    val sections: List<IssuePickerSection>
)