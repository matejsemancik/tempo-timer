package dev.matsem.bpm.data.service

import dev.matsem.bpm.data.model.network.jira.JiraUser
import dev.matsem.bpm.injection.scope.SessionScope

interface JiraApiManager {
    suspend fun getMyself(): JiraUser
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
}