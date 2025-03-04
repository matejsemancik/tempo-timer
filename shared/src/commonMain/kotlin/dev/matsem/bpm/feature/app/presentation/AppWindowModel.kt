package dev.matsem.bpm.feature.app.presentation

import dev.matsem.bpm.arch.BaseModel
import dev.matsem.bpm.data.operation.UndoStack
import dev.matsem.bpm.data.repo.GitHubRepo
import dev.matsem.bpm.data.repo.model.Timer
import dev.matsem.bpm.tooling.Platform
import dev.matsem.bpm.tooling.VersionComparator
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

internal class AppWindowModel(
    private val gitHubRepo: GitHubRepo,
    private val platform: Platform,
    private val undoStack: UndoStack,
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
                val isUpdateAvailable = VersionComparator.isUpdateAvailable(currentVersion, latestAppVersion.version)

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

        override fun onUndo() {
            coroutineScope.launch {
                undoStack.undo()
            }
        }
    }
}
