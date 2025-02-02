package dev.matsem.bpm.injection

import dev.matsem.bpm.data.repo.SessionRepo
import dev.matsem.bpm.data.repo.SessionRepoImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

fun dataModule() = module {
    singleOf(::SessionRepoImpl) bind SessionRepo::class
}