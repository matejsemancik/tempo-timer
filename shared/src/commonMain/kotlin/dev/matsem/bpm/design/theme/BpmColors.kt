package dev.matsem.bpm.design.theme

import androidx.compose.ui.graphics.Color

data class BpmColors(
    val favourite: Color,
    val success: Color,
)

fun lightBpmColors() = BpmColors(
    favourite = Color(0xfff2b21d),
    success = Color(0xff31994e),
)

fun darkBpmColors() = BpmColors(
    favourite = Color(0xffd19917),
    success = Color(0xff52cc73),
)
