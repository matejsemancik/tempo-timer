package dev.matsem.bpm.feature.app.presentation

import dev.matsem.bpm.data.repo.model.Timer

interface AppWindowActions {

    fun onSettingsClick()
    fun onSearchClick()
    fun onOpenCommitDialog(timer: Timer)

    fun onUpdateBannerDismissClick()
    fun onDismissSheet()

    fun onUndo()

    companion object {
        fun noOp() = object : AppWindowActions {
            override fun onSettingsClick() = Unit
            override fun onSearchClick() = Unit
            override fun onOpenCommitDialog(timer: Timer) = Unit
            override fun onUpdateBannerDismissClick() = Unit
            override fun onDismissSheet() = Unit
            override fun onUndo() = Unit
        }
    }
}
