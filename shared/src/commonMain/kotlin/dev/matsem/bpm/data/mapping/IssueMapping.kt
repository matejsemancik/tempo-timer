package dev.matsem.bpm.data.mapping

import dev.matsem.bpm.data.repo.model.Issue as Issue_Domain
import dev.matsem.bpm.data.service.jira.model.issuePicker.SuggestedIssue as SuggestedIssue_Network

internal object IssueMapping {

    fun SuggestedIssue_Network.toDomainModel(apiUrl: String): Issue_Domain = Issue_Domain(
        key = key,
        title = summary,
        iconUrl = apiUrl + iconUrlPath.removePrefix("/")
    )
}