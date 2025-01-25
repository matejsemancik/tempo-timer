package dev.matsem.bpm

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import javax.swing.SwingUtilities

fun main() = application {
    var isOpen by remember { mutableStateOf(true) }
    val windowState = rememberWindowState(
        size = DpSize(600.dp, 400.dp),
    )

    if (isOpen) {
        Window(
            state = windowState,
            onCloseRequest = {
                isOpen = false
            },
            title = "Tempo Desktop",
            resizable = true,
        ) {
            App()
        }
    }

    val trayState = rememberTrayState()

    Tray(
        icon = TrayIcon,
        state = trayState,
        onAction = {
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