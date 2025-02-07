package dev.matsem.bpm.data.repo

import dev.matsem.bpm.data.model.domain.Issue
import dev.matsem.bpm.data.model.mapping.IssueMapping.toDomainModel
import dev.matsem.bpm.data.service.JiraApiManager

interface IssueRepo {

    suspend fun searchIssues(query: String): List<Issue>
}

internal class IssueRepoImpl(
    private val jiraApiManager: JiraApiManager
) : IssueRepo {
    override suspend fun searchIssues(query: String): List<Issue> {
        val response = jiraApiManager.searchIssues(
            query = query,
            currentJql = "issue = \"$query\" OR issue ~ \"$query\" OR text ~ \"$query*\"",
            showSubTasks = true,
            showSubTaskParents = true
        )

        return response.sections
            .flatMap { section -> section.issues }
            .map { issue -> issue.toDomainModel() }
            .distinctBy { issue -> issue.key }
    }
}