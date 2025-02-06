package dev.matsem.bpm.injection.module

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import dev.matsem.bpm.data.persistence.ApplicationPersistence
import dev.matsem.bpm.data.persistence.ApplicationPersistenceImpl
import dev.matsem.bpm.data.persistence.JsonPersistenceHandler
import dev.matsem.bpm.injection.qualifier.Qualifiers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.serialization.json.Json
import okio.Path.Companion.toPath
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

internal expect fun platformPersistenceModule(): Module
internal fun persistenceModule() = module {
    includes(platformPersistenceModule())

    single {
        PreferenceDataStoreFactory.createWithPath(
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
            produceFile = {
                get<String>(named<Qualifiers.DataStoreFilePath>()).toPath()
            }
        )
    }

    single {
        JsonPersistenceHandler(
            dataStore = get(),
            json = Json {
                encodeDefaults = true
                isLenient = false
                ignoreUnknownKeys = true
                prettyPrint = false
            }
        )
    }

    singleOf(::ApplicationPersistenceImpl) bind ApplicationPersistence::class
}