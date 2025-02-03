package dev.matsem.bpm.data.repo

import dev.matsem.bpm.data.model.domain.Credentials
import dev.matsem.bpm.data.model.domain.User
import dev.matsem.bpm.data.model.mapping.CredentialsMapping.toPersistenceModel
import dev.matsem.bpm.data.model.mapping.UserMapping.toDomainModel
import dev.matsem.bpm.data.persistence.ApplicationPersistence
import dev.matsem.bpm.data.service.JiraApiManager
import dev.matsem.bpm.injection.scope.SessionScope

interface SessionRepo : ClearableRepo {

    suspend fun signIn(credentials: Credentials): User
    suspend fun signOut()
    suspend fun fetchUser(): User
}

internal class SessionRepoImpl(
    private val sessionScope: SessionScope,
    private val applicationPersistence: ApplicationPersistence,
    private val jiraApiManager: JiraApiManager,
) : SessionRepo {
    override suspend fun signIn(credentials: Credentials): User {
        sessionScope.closeScope()
        applicationPersistence.saveCredentials(credentials.toPersistenceModel())
        return jiraApiManager.getMyself().toDomainModel()
    }

    override suspend fun signOut() {
        clear()
    }

    override suspend fun fetchUser(): User {
        return jiraApiManager.getMyself().toDomainModel()
    }

    override suspend fun clear() {
        applicationPersistence.deleteCredentials()
        sessionScope.closeScope()
    }
}