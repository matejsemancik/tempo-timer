package dev.matsem.bpm.design.tooling

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.LineHeightStyle

/**
 * Returns copy of this [TextStyle] with [LineHeightStyle] adjusted to trim
 * any excess vertical space to allow for perfect vertical text alignment in containers.
 */
fun TextStyle.centeredVertically() = copy(
    lineHeightStyle = LineHeightStyle(
        LineHeightStyle.Alignment.Center,
        LineHeightStyle.Trim.Both
    )
)