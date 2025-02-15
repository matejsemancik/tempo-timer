package dev.matsem.bpm

import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.Painter
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
        ) {
            AppUi()
        }
    }

    val trayState = rememberTrayState()

    Tray(
        icon = TrayIcon,
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

private object TrayIcon : Painter() {
    override val intrinsicSize: Size = Size(256f, 256f)

    override fun DrawScope.onDraw() {
        drawOval(Color.Magenta)
    }
}