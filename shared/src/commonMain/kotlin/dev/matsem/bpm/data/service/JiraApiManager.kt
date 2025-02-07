package dev.matsem.bpm.data.service

import dev.matsem.bpm.data.model.network.jira.issuePicker.IssuePickerResponse
import dev.matsem.bpm.data.model.network.jira.user.JiraUser
import dev.matsem.bpm.injection.scope.SessionScope

interface JiraApiManager {
    suspend fun getMyself(): JiraUser
    suspend fun searchIssues(
        query: String,
        currentJql: String,
        showSubTasks: Boolean,
        showSubTaskParents: Boolean
    ): IssuePickerResponse
}

internal class JiraApiManagerImpl(
    private val sessionScope: SessionScope,
) : JiraApiManager {

    private suspend fun <T : Any> sessionScoped(block: suspend (api: JiraApi) -> T): T {
        return block(sessionScope.getJiraApi())
    }

    override suspend fun getMyself(): JiraUser = sessionScoped { api ->
        api.getMyself()
    }

    override suspend fun searchIssues(
        query: String,
        currentJql: String,
        showSubTasks: Boolean,
        showSubTaskParents: Boolean
    ): IssuePickerResponse = sessionScoped { api ->
        api.searchIssues(query, currentJql, showSubTasks, showSubTaskParents)
    }
}