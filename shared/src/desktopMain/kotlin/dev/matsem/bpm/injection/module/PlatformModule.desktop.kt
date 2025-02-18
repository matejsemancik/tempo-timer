package dev.matsem.bpm.injection.module

import dev.matsem.bpm.tool.DesktopPlatform
import dev.matsem.bpm.tooling.Platform
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual fun platformModule(): Module = module {
    singleOf(::DesktopPlatform) bind Platform::class
}