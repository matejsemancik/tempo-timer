package dev.matsem.bpm.feature.app.presentation

import dev.matsem.bpm.data.repo.model.Timer

sealed interface AppWindowSheet {
    data object Settings : AppWindowSheet
    data object Search : AppWindowSheet
    data class CommitDialog(val timer: Timer) : AppWindowSheet
}

data class AppWindowState(
    val newVersionBannerVisible: Boolean = false,
    val sheet: AppWindowSheet? = null,
)