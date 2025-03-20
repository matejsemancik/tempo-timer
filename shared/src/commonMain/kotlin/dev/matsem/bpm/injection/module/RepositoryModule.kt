package dev.matsem.bpm.injection.module

import dev.matsem.bpm.data.operation.UndoStack
import dev.matsem.bpm.data.operation.UndoStackImpl
import dev.matsem.bpm.data.repo.GitHubRepo
import dev.matsem.bpm.data.repo.GitHubRepoImpl
import dev.matsem.bpm.data.repo.IssueRepo
import dev.matsem.bpm.data.repo.IssueRepoImpl
import dev.matsem.bpm.data.repo.PreferenceRepo
import dev.matsem.bpm.data.repo.PreferenceRepoImpl
import dev.matsem.bpm.data.repo.SessionRepo
import dev.matsem.bpm.data.repo.SessionRepoImpl
import dev.matsem.bpm.data.repo.TimerRepo
import dev.matsem.bpm.data.repo.TimerRepoImpl
import dev.matsem.bpm.data.repo.WorklogRepo
import dev.matsem.bpm.data.repo.WorklogRepoImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

internal fun repositoryModule() = module {
    singleOf(::SessionRepoImpl) bind SessionRepo::class
    singleOf(::IssueRepoImpl) bind IssueRepo::class
    singleOf(::TimerRepoImpl) bind TimerRepo::class
    singleOf(::WorklogRepoImpl) bind WorklogRepo::class
    singleOf(::GitHubRepoImpl) bind GitHubRepo::class
    singleOf(::PreferenceRepoImpl) bind PreferenceRepo::class

    singleOf(::UndoStackImpl) bind UndoStack::class
}
