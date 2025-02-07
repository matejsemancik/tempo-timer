package dev.matsem.bpm.data.model.mapping

import dev.matsem.bpm.data.model.domain.Issue as Issue_Domain
import dev.matsem.bpm.data.model.network.jira.issuePicker.SuggestedIssue as SuggestedIssue_Network

internal object IssueMapping {

    fun SuggestedIssue_Network.toDomainModel(apiUrl: String): Issue_Domain = Issue_Domain(
        key = key,
        title = summary,
        iconUrl = apiUrl + iconUrlPath.removePrefix("/")
    )
}