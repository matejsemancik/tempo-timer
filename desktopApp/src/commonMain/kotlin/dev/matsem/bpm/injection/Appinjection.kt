package dev.matsem.bpm.injection

import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

internal object AppInjection {
    fun initializeInjection() {
        startKoin {
            modules(
                dataModule(),
                featureModule(),
                networkModule()
            )
        }
    }
}