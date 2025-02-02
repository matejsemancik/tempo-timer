package dev.matsem.bpm.data.repo

import dev.matsem.bpm.data.model.domain.Credentials
import dev.matsem.bpm.data.model.domain.User
import dev.matsem.bpm.data.model.mapping.UserMapping.toDomainModel
import dev.matsem.bpm.data.service.JiraApi
import kotlinx.coroutines.flow.*
import org.koin.core.component.getScopeId
import org.koin.core.component.getScopeName
import org.koin.core.context.GlobalContext
import org.koin.core.scope.Scope

interface SessionRepo : ClearableRepo {
    fun isSignedIn(): Flow<Boolean>
    suspend fun signIn(credentials: Credentials)
    suspend fun signOut()
    suspend fun syncUser()
    fun getUser(): Flow<User?>
}

internal class SessionRepoImpl : SessionRepo {
    private var credentialsScope: Scope? = null
    private val user = MutableStateFlow<User?>(null)
    private val credentials = MutableStateFlow<Credentials?>(null)
    override fun isSignedIn(): Flow<Boolean> = credentials.map { it != null }

    private val jiraApi: JiraApi
        get() = credentialsScope?.get<JiraApi>() ?: error("Scope is not initialized")

    override suspend fun signIn(credentials: Credentials) {
        credentialsScope?.close()
        credentialsScope = GlobalContext.get().createScope(credentials.getScopeId(), credentials.getScopeName(), credentials)
        this.credentials.update { credentials }
        syncUser()
    }

    override suspend fun signOut() {
        clear()
    }

    override suspend fun syncUser() {
        this.user.update {
            jiraApi.getMyself().toDomainModel()
        }
    }

    override fun getUser(): Flow<User?> = user

    override fun clear() {
        this.credentials.update { null }
        this.user.update { null }
        credentialsScope?.close()
    }
}