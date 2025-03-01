package dev.matsem.bpm.design.chip

import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.matsem.bpm.design.theme.BpmTheme

@Composable
fun AppSuggestionChip(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    SuggestionChip(
        modifier = modifier,
        onClick = onClick,
        label = { Text(text = label) },
        shape = BpmTheme.shapes.small,
        colors = SuggestionChipDefaults.suggestionChipColors(
            containerColor = BpmTheme.colorScheme.surfaceContainerHigh
        ),
        border = null,
    )
}