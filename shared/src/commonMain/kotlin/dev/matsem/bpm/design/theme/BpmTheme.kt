package dev.matsem.bpm.design.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf

@Composable
fun BpmTheme(
    isDark: Boolean = isSystemInDarkTheme(),
    dimensions: BpmDimensions = desktopBpmDimensions(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (isDark) {
            darkColorScheme()
        } else {
            lightColorScheme()
        },
    ) {
        CompositionLocalProvider(
            LocalBpmDimensions provides dimensions
        ) {
            content()
        }
    }
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

    val dimensions: BpmDimensions
        @Composable
        @ReadOnlyComposable
        get() = LocalBpmDimensions.current
}

val LocalBpmDimensions = staticCompositionLocalOf<BpmDimensions> { error("BpmDimensions not provided") }