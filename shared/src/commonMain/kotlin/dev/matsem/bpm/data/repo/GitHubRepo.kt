package dev.matsem.bpm.data.repo

import dev.matsem.bpm.data.mapping.toAppVersion
import dev.matsem.bpm.data.repo.model.AppVersion
import dev.matsem.bpm.data.service.github.GitHubApiManager

interface GitHubRepo {
    suspend fun getLatestAppVersion(): AppVersion
}

class GitHubRepoImpl(
    private val gitHubApiManager: GitHubApiManager
) : GitHubRepo {
    
    override suspend fun getLatestAppVersion(): AppVersion {
        return gitHubApiManager.getLatestRelease().toAppVersion()
    }
}