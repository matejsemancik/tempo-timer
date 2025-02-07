package dev.matsem.bpm.design.theme

import androidx.compose.ui.unit.Dp

data class BpmDimensions(
    val horizontalContentPadding: Dp
)

fun desktopBpmDimensions() = BpmDimensions(
    horizontalContentPadding = Grid.d3
)