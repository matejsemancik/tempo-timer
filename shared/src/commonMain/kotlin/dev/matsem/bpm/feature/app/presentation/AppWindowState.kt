package dev.matsem.bpm.feature.app.presentation

import dev.matsem.bpm.data.repo.model.AppThemeMode
import dev.matsem.bpm.data.repo.model.AppVersion
import dev.matsem.bpm.data.repo.model.Timer
import dev.matsem.bpm.design.navigation.NavigationBarItem
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class AppWindowState(
    val newVersionBannerVisible: Boolean = false,
    val latestAppVersion: AppVersion? = null,
    val navigationState: AppWindowNavigationState = AppWindowNavigationState(
        content = AppWindowContent.Timer,
        sheet = null
    ),
    val themeMode: AppThemeMode = AppThemeMode.SYSTEM,
) {
    val navigationItems: ImmutableList<NavigationBarItem>
        get() = persistentListOf(
            NavigationBarItem.Timer(isSelected = navigationState.content == AppWindowContent.Timer),
//            NavigationBarItem.Logbook(isSelected = navigationState.content == AppWindowContent.Logbook),
            NavigationBarItem.Settings(isSelected = navigationState.content == AppWindowContent.Settings),
        )

    val isFabVisible: Boolean
        get() = navigationState.content == AppWindowContent.Timer
}

data class AppWindowNavigationState(
    val content: AppWindowContent,
    val sheet: AppWindowSheet? = null,
)

sealed interface AppWindowContent {
    data object Timer : AppWindowContent
    data object Logbook : AppWindowContent
    data object Settings : AppWindowContent
}

sealed interface AppWindowSheet {
    data object Search : AppWindowSheet
    data class CommitDialog(val timer: Timer) : AppWindowSheet
}
