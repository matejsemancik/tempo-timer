package dev.matsem.bpm.design.input

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.matsem.bpm.design.theme.BpmTheme
import dev.matsem.bpm.design.theme.Grid
import dev.matsem.bpm.design.tooling.VerticalSpacer

@Composable
fun LabeledTextField(
    label: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Column(modifier) {
        Text(text = label, style = BpmTheme.typography.labelLarge)
        VerticalSpacer(Grid.d1)
        content()
    }
}