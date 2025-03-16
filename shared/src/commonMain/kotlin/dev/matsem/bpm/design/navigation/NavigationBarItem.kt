package dev.matsem.bpm.design.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Timer
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import bpm_tracker.shared.generated.resources.Res
import bpm_tracker.shared.generated.resources.bottom_nav_logbook
import bpm_tracker.shared.generated.resources.bottom_nav_settings
import bpm_tracker.shared.generated.resources.bottom_nav_timer
import org.jetbrains.compose.resources.stringResource

sealed interface NavigationBarItem {

    val title: String @Composable get
    val icon: Painter @Composable get
    val isSelected: Boolean

    data class Timer(override val isSelected: Boolean = false) : NavigationBarItem {
        override val title: String
            @Composable get() = stringResource(Res.string.bottom_nav_timer)
        override val icon: Painter
            @Composable get() = rememberVectorPainter(Icons.Filled.Timer)
    }

    data class Logbook(override val isSelected: Boolean = false) : NavigationBarItem {
        override val title: String
            @Composable get() = stringResource(Res.string.bottom_nav_logbook)
        override val icon: Painter
            @Composable get() = rememberVectorPainter(Icons.Filled.Receipt)
    }

    data class Settings(override val isSelected: Boolean = false) : NavigationBarItem {
        override val title: String
            @Composable get() = stringResource(Res.string.bottom_nav_settings)
        override val icon: Painter
            @Composable get() = rememberVectorPainter(Icons.Filled.Settings)
    }
}
