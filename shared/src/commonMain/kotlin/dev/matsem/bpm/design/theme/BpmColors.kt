package dev.matsem.bpm.design.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val ColorScheme.favourite: Color
    @Composable
    get() = when (isSystemInDarkTheme()) {
        true -> Color(0xffd19917)
        false -> Color(0xfff2b21d)
    }