package dev.matsem.bpm.design.tooling

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import dev.matsem.bpm.design.theme.BpmTheme

@Composable
fun Showcase(isDark: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    BpmTheme(isDark = isDark) {
        Surface(color = MaterialTheme.colorScheme.background) {
            content()
        }
    }
}