package dev.matsem.bpm.feature.app.presentation

import dev.matsem.bpm.data.repo.model.Timer
import dev.matsem.bpm.design.navigation.NavigationBarItem

interface AppWindowActions {

    fun onNavigationBarClick(item: NavigationBarItem)
    fun onNewTimerClick()
    fun onOpenCommitDialog(timer: Timer)

    fun onUpdateBannerDismissClick()
    fun onDismissSheet()

    fun onUndo()

    companion object {
        fun noOp() = object : AppWindowActions {
            override fun onNavigationBarClick(item: NavigationBarItem) = Unit
            override fun onNewTimerClick() = Unit
            override fun onOpenCommitDialog(timer: Timer) = Unit
            override fun onUpdateBannerDismissClick() = Unit
            override fun onDismissSheet() = Unit
            override fun onUndo() = Unit
        }
    }
}
