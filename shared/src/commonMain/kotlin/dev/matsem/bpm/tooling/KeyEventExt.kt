package dev.matsem.bpm.tooling

import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.isCtrlPressed
import androidx.compose.ui.input.key.isMetaPressed

/**
 * Extension function to check if either the Ctrl key (on Windows/Linux) or the Meta key (on macOS) is pressed.
 *
 * @param platform The platform instance used to determine the operating system.
 * @return `true` if the appropriate key is pressed based on the operating system, `false` otherwise.
 */
fun KeyEvent.isMetaOrCtrlPressed(platform: Platform) = when (platform.isMacos()) {
    true -> isMetaPressed
    false -> isCtrlPressed
}
