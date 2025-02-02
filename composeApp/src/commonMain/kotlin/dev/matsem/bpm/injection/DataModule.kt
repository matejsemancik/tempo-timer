package dev.matsem.bpm.injection

import dev.matsem.bpm.data.repo.CredentialsRepo
import dev.matsem.bpm.data.repo.CredentialsRepoImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

fun dataModule() = module {
    singleOf(::CredentialsRepoImpl) bind CredentialsRepo::class
}