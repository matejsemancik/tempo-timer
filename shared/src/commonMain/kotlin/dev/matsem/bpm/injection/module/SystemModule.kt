package dev.matsem.bpm.injection.module

import kotlinx.datetime.Clock
import org.koin.dsl.module

fun systemModule() = module {
    single<Clock> { Clock.System }
}