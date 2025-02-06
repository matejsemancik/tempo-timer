package dev.matsem.bpm.data.repo

import dev.matsem.bpm.data.database.dao.UserDao
import dev.matsem.bpm.data.model.domain.Credentials
import dev.matsem.bpm.data.model.domain.User
import dev.matsem.bpm.data.model.mapping.CredentialsMapping.toPersistenceModel
import dev.matsem.bpm.data.model.mapping.UserMapping.toDbInsert
import dev.matsem.bpm.data.model.mapping.UserMapping.toDomainModel
import dev.matsem.bpm.data.persistence.ApplicationPersistence
import dev.matsem.bpm.data.service.JiraApiManager
import dev.matsem.bpm.injection.scope.SessionScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

interface SessionRepo {

    suspend fun signIn(credentials: Credentials)
    suspend fun signOut()
    suspend fun syncUser()

    fun getUser(): Flow<User?>
}

internal class SessionRepoImpl(
    private val sessionScope: SessionScope,
    private val applicationPersistence: ApplicationPersistence,
    private val userDao: UserDao,
    private val jiraApiManager: JiraApiManager,
) : SessionRepo {

    override suspend fun signIn(credentials: Credentials) {
        sessionScope.closeScope()
        applicationPersistence.saveCredentials(credentials.toPersistenceModel())
        syncUser()
    }

    override suspend fun signOut() {
        applicationPersistence.deleteCredentials()
        sessionScope.closeScope()
        userDao.delete()
    }

    override suspend fun syncUser() {
        jiraApiManager.getMyself().toDbInsert().let { userDao.upsert(it) }
    }

    override fun getUser(): Flow<User?> = flow {
        emit(userDao.get()?.toDomainModel())
        runCatching { syncUser() }
        userDao.observe().map { it?.toDomainModel() }.collect(this)
    }
}