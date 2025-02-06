package dev.matsem.bpm.injection.module

import dev.matsem.bpm.injection.scope.SessionScope
import dev.matsem.bpm.injection.scope.SessionScopeImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

fun scopeModule() = module {
    singleOf(::SessionScopeImpl) bind SessionScope::class
}