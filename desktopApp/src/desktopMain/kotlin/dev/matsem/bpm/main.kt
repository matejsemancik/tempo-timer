package dev.matsem.bpm

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Timer
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.key.*
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import dev.matsem.bpm.feature.app.ui.AppUi
import dev.matsem.bpm.injection.AppInjection
import org.koin.compose.KoinContext

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
        position = WindowPosition(Alignment.Center)
    )

    if (isOpen) {
        Window(
            state = windowState,
            onCloseRequest = {
                isOpen = false
            },
            title = "Tempo Timer",
            resizable = true,
            onPreviewKeyEvent = { keyEvent ->
                when {
                    keyEvent.type == KeyEventType.KeyDown && keyEvent.isMetaPressed && keyEvent.key == Key.W -> {
                        isOpen = false
                        true
                    }
                    else -> false
                }
            }
        ) {
            AppUi()
        }
    }

    val trayState = rememberTrayState()

    Tray(
        icon = rememberVectorPainter(Icons.Rounded.Timer),
        state = trayState,
        onAction = {
            println("onAction")
            isOpen = true
        },
        menu = {
            Item("Open Tracker…", onClick = {
                isOpen = true
            })
            Separator()
            Item("Quit", onClick = ::exitApplication)
        }
    )
}
