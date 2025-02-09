package dev.matsem.bpm.data.mapping

import dev.matsem.bpm.data.database.model.JiraIssue as JiraIssue_Database
import dev.matsem.bpm.data.repo.model.Issue as JiraIssue_Domain
import dev.matsem.bpm.data.service.jira.model.issuePicker.SuggestedIssue as JiraIssue_Network

internal object IssueMapping {

    fun JiraIssue_Network.toDomainModel(apiUrl: String): JiraIssue_Domain = JiraIssue_Domain(
        id = id,
        key = key,
        summary = summary,
        iconUrl = apiUrl + iconUrlPath.removePrefix("/")
    )

    fun JiraIssue_Domain.toDbModel(): JiraIssue_Database = JiraIssue_Database(
        id = id,
        key = key,
        sumary = summary,
        iconUrl = iconUrl
    )

    fun JiraIssue_Database.toDomainModel(): JiraIssue_Domain = JiraIssue_Domain(
        id = id,
        key = key,
        summary = sumary,
        iconUrl = iconUrl
    )
}