package dev.matsem.bpm.injection.module

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import org.koin.dsl.module

fun systemModule() = module {
    single<Clock> { Clock.System }
    single<TimeZone> { TimeZone.currentSystemDefault() }
}
