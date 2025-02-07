package dev.matsem.bpm.data.repo

import dev.matsem.bpm.data.model.domain.Issue
import dev.matsem.bpm.data.model.mapping.CredentialsMapping.toDomainModel
import dev.matsem.bpm.data.model.mapping.IssueMapping.toDomainModel
import dev.matsem.bpm.data.persistence.ApplicationPersistence
import dev.matsem.bpm.data.service.JiraApiManager

interface IssueRepo {

    suspend fun searchIssues(query: String): List<Issue>
}

internal class IssueRepoImpl(
    private val jiraApiManager: JiraApiManager,
    private val applicationPersistence: ApplicationPersistence,
) : IssueRepo {
    override suspend fun searchIssues(query: String): List<Issue> {
        val response = jiraApiManager.searchIssues(
            query = query,
            currentJql = "issue = \"$query\" OR issue ~ \"$query\" OR text ~ \"$query*\"",
            showSubTasks = true,
            showSubTaskParents = true
        )

        val baseApiUrl = applicationPersistence.getCredentials()?.toDomainModel()?.jiraApiHost
            ?: error("Credentials are not present")

        return response.sections
            .flatMap { section -> section.issues }
            .map { issue -> issue.toDomainModel(baseApiUrl) }
            .distinctBy { issue -> issue.key }
    }
}