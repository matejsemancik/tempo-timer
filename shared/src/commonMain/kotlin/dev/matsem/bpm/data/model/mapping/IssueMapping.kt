package dev.matsem.bpm.data.model.mapping

import dev.matsem.bpm.data.model.domain.IssueType
import dev.matsem.bpm.data.model.domain.Issue as Issue_Domain
import dev.matsem.bpm.data.model.network.jira.issuePicker.SuggestedIssue as SuggestedIssue_Network

internal object IssueMapping {

    fun SuggestedIssue_Network.toDomainModel(): Issue_Domain = Issue_Domain(
        IssueType.Task,
        key = key,
        title = summary
    )
}