package dev.matsem.bpm.design.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable

@Composable
fun BpmTheme(isDark: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (isDark) {
            darkColorScheme()
        } else {
            lightColorScheme()
        },
        content = content
    )

    MaterialTheme
}

object BpmTheme {

    val colorScheme: ColorScheme
        @Composable
        @ReadOnlyComposable
        get() = MaterialTheme.colorScheme

    val typography: Typography
        @Composable
        @ReadOnlyComposable
        get() = MaterialTheme.typography

    val shapes: Shapes
        @Composable
        @ReadOnlyComposable
        get() = MaterialTheme.shapes
}