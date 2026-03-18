package dev.matsem.bpm.injection.module

import kotlinx.datetime.TimeZone
import org.koin.dsl.module
import kotlin.time.Clock

fun systemModule() = module {
    single<Clock> { Clock.System }
    single<TimeZone> { TimeZone.currentSystemDefault() }
}
