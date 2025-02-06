package dev.matsem.bpm.injection.module

import dev.matsem.bpm.injection.qualifier.Qualifiers
import dev.matsem.bpm.tool.FileProvider
import org.koin.core.module.Module
import org.koin.core.module.dsl.named
import org.koin.core.module.dsl.withOptions
import org.koin.dsl.module
import kotlin.io.path.absolutePathString

internal actual fun platformPersistenceModule(): Module = module {
    single {
        FileProvider.getApplicationDataPath().resolve("app_datastore.preferences_pb").absolutePathString()
    } withOptions { named<Qualifiers.DataStoreFilePath>() }
}