package dev.matsem.bpm.data.persistence

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json

/**
 * [DataStore]-backed Persistence which allows storage and observing of complex JSON objects.
 * Uses kx.serialization to serialize and deserialize objects into Strings.
 */
internal class JsonPersistenceHandler(
    private val dataStore: DataStore<Preferences>,
    private val json: Json,
) {

    /**
     * Saves provided [value] in persistence under provided [key].
     */
    suspend inline fun <reified T : Any> save(
        key: Preferences.Key<String>,
        value: T,
    ) {
        dataStore.edit { preferences -> preferences[key] = json.encodeToString<T>(value) }
    }

    /**
     * Returns persisted object with provided [key].
     *
     * @return Persisted object, or `null` if does not exist.
     * @throws [SerializationException] if object could not be deserialized as [T].
     */
    suspend inline fun <reified T : Any> get(key: Preferences.Key<String>): T? =
        dataStore.data.firstOrNull()
            ?.let { preferences -> preferences[key] }
            ?.let { jsonString ->
                json.decodeFromString<T>(jsonString)
            }

    /**
     * Returns a [Flow] of persisted objects with provided [key].
     *
     * @return [Flow] of objects. Values in [Flow] can be `null` if not found in persistence.
     * @throws [SerializationException] if object could not be deserialized as [T].
     */
    inline fun <reified T : Any> observe(key: Preferences.Key<String>): Flow<T?> =
        dataStore.data
            .map { preferences -> preferences[key] }
            .map { jsonString ->
                jsonString?.let { json.decodeFromString(jsonString) }
            }

    /**
     * Removes persisted object with provided [key].
     */
    suspend fun delete(key: Preferences.Key<String>) {
        dataStore.edit { preferences -> preferences.remove(key) }
    }

    /**
     * Clears entire persistence.
     */
    suspend fun clear() {
        dataStore.edit { prefs -> prefs.clear() }
    }
}