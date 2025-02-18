package dev.matsem.bpm.injection

import dev.matsem.bpm.injection.module.databaseModule
import dev.matsem.bpm.injection.module.featureModule
import dev.matsem.bpm.injection.module.networkModule
import dev.matsem.bpm.injection.module.persistenceModule
import dev.matsem.bpm.injection.module.platformModule
import dev.matsem.bpm.injection.module.repositoryModule
import dev.matsem.bpm.injection.module.scopeModule
import dev.matsem.bpm.injection.module.systemModule
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.core.logger.PrintLogger

object AppInjection {
    fun initializeInjection() {
        startKoin {
            logger(PrintLogger(Level.ERROR))

            modules(
                platformModule(),
                systemModule(),
                scopeModule(),
                repositoryModule(),
                networkModule(),
                persistenceModule(),
                databaseModule(),
                featureModule(),
            )
        }
    }
}