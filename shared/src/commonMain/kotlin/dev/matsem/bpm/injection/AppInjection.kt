package dev.matsem.bpm.injection

import dev.matsem.bpm.injection.module.*
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.core.logger.PrintLogger

object AppInjection {
    fun initializeInjection() {
        startKoin {
            logger(PrintLogger(Level.ERROR))

            modules(
                scopeModule(),
                repositoryModule(),
                networkModule(),
                persistenceModule(),
                featureModule(),
            )
        }
    }
}