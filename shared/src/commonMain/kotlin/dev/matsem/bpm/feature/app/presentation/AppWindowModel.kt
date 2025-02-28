package dev.matsem.bpm.feature.app.presentation

import dev.matsem.bpm.arch.BaseModel
import dev.matsem.bpm.data.repo.GitHubRepo
import dev.matsem.bpm.data.repo.model.Timer
import dev.matsem.bpm.tooling.Platform
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

internal class AppWindowModel(
    private val gitHubRepo: GitHubRepo,
    private val platform: Platform,
) : BaseModel<AppWindowState, Nothing>(
    defaultState = AppWindowState(newVersionBannerVisible = false)
), AppWindow, KoinComponent {

    init {
        checkForUpdates()
    }

    private fun checkForUpdates() {
        coroutineScope.launch {
            runCatching {
                val latestAppVersion = gitHubRepo.getLatestAppVersion()
                val currentVersion = platform.getVersionString()
                val isUpdateAvailable = isUpdateAvailable(currentVersion, latestAppVersion.version)

                println("latestAppVersion: $latestAppVersion")
                println("currentVersion: $currentVersion")
                println("isUpdateAvailable: $isUpdateAvailable")

                updateState { state ->
                    state.copy(
                        newVersionBannerVisible = isUpdateAvailable,
                        latestAppVersion = latestAppVersion
                    )
                }
            }.onFailure { error -> print(error) }
        }
    }

    /**
     * Compare version strings to determine if an update is available.
     * Expected format: 1.2.3
     */
    private fun isUpdateAvailable(currentVersion: String, latestVersion: String): Boolean {
        val current = parseVersion(currentVersion)
        val latest = parseVersion(latestVersion)

        // Compare major version
        if (latest[0] > current[0]) return true
        if (latest[0] < current[0]) return false

        // Compare minor version
        if (latest[1] > current[1]) return true
        if (latest[1] < current[1]) return false

        // Compare patch version
        return latest[2] > current[2]
    }

    private fun parseVersion(version: String): List<Int> {
        return version.split('.')
            .mapNotNull { it.toIntOrNull() }
            .takeIf { it.size >= 3 } // Ensure we have at least major.minor.patch
            ?: listOf(0, 0, 0) // Default to 0.0.0 if parsing fails
    }

    override val actions: AppWindowActions = object : AppWindowActions {
        override fun onSettingsClick() = updateState { state ->
            state.copy(sheet = AppWindowSheet.Settings)
        }

        override fun onSearchClick() = updateState { state ->
            state.copy(sheet = AppWindowSheet.Search)
        }

        override fun onOpenCommitDialog(timer: Timer) = updateState { state ->
            state.copy(sheet = AppWindowSheet.CommitDialog(timer))
        }

        override fun onUpdateBannerDismissClick() = updateState { state ->
            state.copy(newVersionBannerVisible = false)
        }

        override fun onDismissSheet() = updateState { state ->
            state.copy(sheet = null)
        }
    }
}