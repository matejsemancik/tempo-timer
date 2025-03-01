package dev.matsem.bpm.data.service.github

import dev.matsem.bpm.data.service.github.model.Release

interface GitHubApiManager {
    suspend fun getLatestRelease(): Release
}

internal class GitHubApiManagerImpl(private val api: GitHubApi) : GitHubApiManager {

    override suspend fun getLatestRelease(): Release {
        return api.getLatestRelease()
    }
}