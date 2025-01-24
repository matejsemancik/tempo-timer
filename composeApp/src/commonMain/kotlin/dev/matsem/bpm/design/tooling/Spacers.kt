package dev.matsem.bpm.design.tooling

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

@Composable
fun RowScope.horizontalSpacer(size: Dp) = Spacer(Modifier.width(size))

@Composable
fun ColumnScope.verticalSpacer(size: Dp) = Spacer(Modifier.height(size))