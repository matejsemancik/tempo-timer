package dev.matsem.bpm.data.service.github

import de.jensklingenberg.ktorfit.http.GET
import dev.matsem.bpm.data.service.github.model.Release

interface GitHubApi {

    @GET("repos/matejsemancik/tempo-timer/releases/latest")
    suspend fun getLatestRelease(): Release

}