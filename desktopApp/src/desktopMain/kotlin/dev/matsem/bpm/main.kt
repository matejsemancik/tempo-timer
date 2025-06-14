package dev.matsem.bpm

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.toAwtImage
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import bpm_tracker.desktopapp.generated.resources.Res
import bpm_tracker.desktopapp.generated.resources.launcher_icon
import dev.matsem.bpm.feature.app.presentation.AppWindow
import dev.matsem.bpm.feature.app.ui.AppWindowUi
import dev.matsem.bpm.injection.AppInjection
import dev.matsem.bpm.tooling.Platform
import dev.matsem.bpm.tooling.isMetaOrCtrlPressed
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.KoinContext
import org.koin.compose.koinInject
import java.awt.Desktop
import java.awt.Taskbar
import java.awt.desktop.AppReopenedListener

fun main() {
    AppInjection.initializeInjection()
    application {
        KoinContext {
            MainApplication()
        }
    }
}

@Composable
fun ApplicationScope.MainApplication() {
    var isOpen by remember { mutableStateOf(true) }
    val windowState = rememberWindowState(
        size = DpSize(480.dp, 640.dp),
        position = WindowPosition(Alignment.Center),
    )

    DisposableEffect(Unit) {
        val listener = AppReopenedListener { p0 ->
            isOpen = true
        }
        Desktop.getDesktop().addAppEventListener(listener)
        onDispose {
            Desktop.getDesktop().removeAppEventListener(listener)
        }
    }

    // On macOS, the launcher icon needs to be added using AWT Taskbar
    val density = LocalDensity.current
    val layoutDirection = LocalLayoutDirection.current
    val launcherIcon = painterResource(Res.drawable.launcher_icon)
    LaunchedEffect(Unit) {
        if (Taskbar.isTaskbarSupported()) {
            Taskbar.getTaskbar().iconImage = launcherIcon.toAwtImage(
                density = density,
                layoutDirection = layoutDirection
            )
        }
    }

    val appWindow: AppWindow = koinInject()
    val platform: Platform = koinInject()

    Window(
        state = windowState,
        onCloseRequest = {
            isOpen = false
        },
        icon = painterResource(Res.drawable.launcher_icon),
        title = "Tempo Timer",
        resizable = true,
        visible = isOpen,
        onPreviewKeyEvent = { keyEvent ->
            when {
                keyEvent.type != KeyEventType.KeyDown -> {
                    false
                }

                keyEvent.isMetaOrCtrlPressed(platform) && keyEvent.key == Key.W -> {
                    isOpen = false
                    true
                }

                keyEvent.isMetaOrCtrlPressed(platform) && keyEvent.key == Key.Z -> {
                    appWindow.actions.onUndo()
                    true
                }

                else -> false
            }
        }
    ) {
        AppWindowUi(window = appWindow)
    }
}
