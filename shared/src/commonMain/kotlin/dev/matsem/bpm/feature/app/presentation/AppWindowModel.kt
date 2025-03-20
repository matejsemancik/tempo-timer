package dev.matsem.bpm.feature.app.presentation

import dev.matsem.bpm.arch.BaseModel
import dev.matsem.bpm.data.operation.UndoStack
import dev.matsem.bpm.data.repo.GitHubRepo
import dev.matsem.bpm.data.repo.PreferenceRepo
import dev.matsem.bpm.data.repo.model.Timer
import dev.matsem.bpm.design.navigation.NavigationBarItem
import dev.matsem.bpm.tooling.Platform
import dev.matsem.bpm.tooling.VersionComparator
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

internal class AppWindowModel(
    private val gitHubRepo: GitHubRepo,
    private val platform: Platform,
    private val undoStack: UndoStack,
    private val preferenceRepo: PreferenceRepo,
) : BaseModel<AppWindowState, Nothing>(
    defaultState = AppWindowState(newVersionBannerVisible = false)
), AppWindow, KoinComponent {

    override suspend fun onStart() {
        checkForUpdates()
        observeAppThemeMode()
    }

    private fun checkForUpdates() {
        coroutineScope.launch {
            runCatching {
                val latestAppVersion = gitHubRepo.getLatestAppVersion()
                val currentVersion = platform.getVersionString()
                val isUpdateAvailable = VersionComparator.isUpdateAvailable(currentVersion, latestAppVersion.version)

                updateState { state ->
                    state.copy(
                        newVersionBannerVisible = isUpdateAvailable,
                        latestAppVersion = latestAppVersion
                    )
                }
            }.onFailure { error -> print(error) }
        }
    }

    private fun observeAppThemeMode() = coroutineScope.launch {
        preferenceRepo.observeAppThemeMode().collect { mode ->
            updateState { state ->
                state.copy(themeMode = mode)
            }
        }
    }

    override val actions: AppWindowActions = object : AppWindowActions {

        override fun onNavigationBarClick(item: NavigationBarItem) = updateState { state ->
            state.copy(
                navigationState = state.navigationState.copy(
                    content = when (item) {
                        is NavigationBarItem.Timer -> AppWindowContent.Timer
                        is NavigationBarItem.Logbook -> TODO("https://github.com/matejsemancik/tempo-timer/issues/7")
                        is NavigationBarItem.Settings -> AppWindowContent.Settings
                    }
                )
            )
        }

        override fun onNewTimerClick() = updateState { state ->
            state.copy(navigationState = state.navigationState.copy(sheet = AppWindowSheet.Search))
        }

        override fun onOpenCommitDialog(timer: Timer) = updateState { state ->
            state.copy(navigationState = state.navigationState.copy(sheet = AppWindowSheet.CommitDialog(timer)))
        }

        override fun onUpdateBannerDismissClick() = updateState { state ->
            state.copy(newVersionBannerVisible = false)
        }

        override fun onDismissSheet() = updateState { state ->
            state.copy(navigationState = state.navigationState.copy(sheet = null))
        }

        override fun onUndo() {
            coroutineScope.launch {
                undoStack.undo()
            }
        }
    }
}
