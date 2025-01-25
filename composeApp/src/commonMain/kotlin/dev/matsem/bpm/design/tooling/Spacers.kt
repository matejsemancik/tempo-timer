package dev.matsem.bpm.design.tooling

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

@Composable
fun RowScope.HorizontalSpacer(size: Dp) = Spacer(Modifier.width(size))

@Composable
fun ColumnScope.VerticalSpacer(size: Dp) = Spacer(Modifier.height(size))