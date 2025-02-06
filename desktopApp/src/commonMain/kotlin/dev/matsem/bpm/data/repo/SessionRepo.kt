package dev.matsem.bpm.data.repo

import dev.matsem.bpm.data.model.domain.Credentials
import dev.matsem.bpm.data.model.domain.User
import dev.matsem.bpm.data.model.mapping.CredentialsMapping.toPersistenceModel
import dev.matsem.bpm.data.model.mapping.UserMapping.toDomainModel
import dev.matsem.bpm.data.persistence.ApplicationPersistence
import dev.matsem.bpm.data.service.JiraApiManager
import dev.matsem.bpm.injection.scope.SessionScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update

interface SessionRepo {

    suspend fun signIn(credentials: Credentials)
    suspend fun signOut()
    suspend fun syncUser()

    fun getUser(): Flow<User?>
}

internal class SessionRepoImpl(
    private val sessionScope: SessionScope,
    private val applicationPersistence: ApplicationPersistence,
    private val jiraApiManager: JiraApiManager,
) : SessionRepo {

    private val user = MutableStateFlow<User?>(null)
    override suspend fun signIn(credentials: Credentials) {
        sessionScope.closeScope()
        applicationPersistence.saveCredentials(credentials.toPersistenceModel())
        syncUser()
    }

    override suspend fun signOut() {
        applicationPersistence.deleteCredentials()
        sessionScope.closeScope()
        user.update { null }
    }

    override suspend fun syncUser() {
        user.update { jiraApiManager.getMyself().toDomainModel() }
    }

    override fun getUser(): Flow<User?> = flow {
        emit(user.value)
        runCatching { syncUser() }
        user.collect(this)
    }
}