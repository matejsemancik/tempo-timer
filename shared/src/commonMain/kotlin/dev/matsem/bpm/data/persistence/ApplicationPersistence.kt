package dev.matsem.bpm.data.persistence

import androidx.datastore.preferences.core.stringPreferencesKey
import dev.matsem.bpm.data.persistence.model.Credentials
import kotlinx.coroutines.flow.Flow

interface ApplicationPersistence {

    suspend fun saveCredentials(credentials: Credentials)

    suspend fun getCredentials(): Credentials?

    suspend fun observeCredentials(): Flow<Credentials?>

    suspend fun deleteCredentials()

    suspend fun saveDarkMode(darkMode: Boolean)

    fun observeDarkMode(): Flow<Boolean?>

    suspend fun clearDarkMode()

    suspend fun clear()
}

internal class ApplicationPersistenceImpl(
    private val handler: JsonPersistenceHandler,
) : ApplicationPersistence {

    companion object {
        private val CredentialsKey = stringPreferencesKey("credentials")
        private val DarkModeKey = stringPreferencesKey("dark_mode")
    }

    override suspend fun saveCredentials(credentials: Credentials) = handler.save(CredentialsKey, credentials)

    override suspend fun getCredentials(): Credentials? = handler.get(CredentialsKey)

    override suspend fun observeCredentials(): Flow<Credentials?> = handler.observe(CredentialsKey)

    override suspend fun deleteCredentials() = handler.delete(CredentialsKey)

    override suspend fun saveDarkMode(darkMode: Boolean) = handler.save(DarkModeKey, darkMode)

    override suspend fun clearDarkMode() = handler.delete(DarkModeKey)

    override fun observeDarkMode(): Flow<Boolean?> = handler.observe(DarkModeKey)

    override suspend fun clear() = handler.clear()
}
