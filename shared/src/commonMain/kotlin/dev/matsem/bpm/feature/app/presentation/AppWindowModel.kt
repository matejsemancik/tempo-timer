package dev.matsem.bpm.feature.app.presentation

import dev.matsem.bpm.arch.BaseModel
import dev.matsem.bpm.data.repo.model.Timer

internal class AppWindowModel : BaseModel<AppWindowState, Nothing>(
    defaultState = AppWindowState(newVersionBannerVisible = true)
), AppWindow {

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