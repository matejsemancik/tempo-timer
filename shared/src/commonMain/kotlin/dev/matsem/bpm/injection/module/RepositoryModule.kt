package dev.matsem.bpm.injection.module

import dev.matsem.bpm.data.repo.*
import dev.matsem.bpm.data.repo.IssueRepoImpl
import dev.matsem.bpm.data.repo.SessionRepoImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

internal fun repositoryModule() = module {
    singleOf(::SessionRepoImpl) bind SessionRepo::class
    singleOf(::IssueRepoImpl) bind IssueRepo::class
    singleOf(::TimerRepoImpl) bind TimerRepo::class
}