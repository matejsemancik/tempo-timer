package dev.matsem.bpm.injection.module

import dev.matsem.bpm.injection.qualifier.Qualifiers
import org.koin.core.module.Module
import org.koin.core.module.dsl.named
import org.koin.core.module.dsl.withOptions
import org.koin.dsl.module

internal actual fun platformPersistenceModule(): Module = module {
    single { "files/persistence.preferences_pb" } withOptions { named<Qualifiers.DataStoreFilePath>() }
}