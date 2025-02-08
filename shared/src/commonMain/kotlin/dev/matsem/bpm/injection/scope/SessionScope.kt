package dev.matsem.bpm.injection.scope

import dev.matsem.bpm.data.mapping.CredentialsMapping.toDomainModel
import dev.matsem.bpm.data.repo.model.Credentials
import dev.matsem.bpm.data.persistence.ApplicationPersistence
import dev.matsem.bpm.data.service.jira.JiraApi
import org.koin.core.Koin
import org.koin.core.context.GlobalContext
import org.koin.core.qualifier.named

interface SessionScope {

    fun closeScope()
    suspend fun getJiraApi(): JiraApi
}

internal class SessionScopeImpl(
    private val applicationPersistence: ApplicationPersistence
) : SessionScope {

    companion object {
        private const val SessionScopeId = "session_scope"
        private val SessionScopeName = named<Credentials>()
    }

    private val koin: Koin
        get() = GlobalContext.get()

    override fun closeScope() {
        koin.getScopeOrNull(SessionScopeId)?.close()
    }

    override suspend fun getJiraApi(): JiraApi {
        val credentials = applicationPersistence.getCredentials() ?: run {
            koin.getScopeOrNull(SessionScopeId)?.close()
            error("No credentials")
        }

        val scope = koin.getOrCreateScope(SessionScopeId, SessionScopeName, credentials.toDomainModel())
        return scope.get()
    }
}