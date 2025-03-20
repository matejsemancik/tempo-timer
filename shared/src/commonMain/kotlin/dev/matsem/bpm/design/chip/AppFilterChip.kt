package dev.matsem.bpm.design.chip

import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.matsem.bpm.design.theme.BpmTheme

@Composable
fun AppFilterChip(
    selected: Boolean,
    label: String,
    leadingIcon: (@Composable () -> Unit)? = null,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    FilterChip(
        selected = selected,
        modifier = modifier,
        onClick = onClick,
        label = { Text(text = label) },
        shape = BpmTheme.shapes.small,
        colors = FilterChipDefaults.filterChipColors(
            containerColor = BpmTheme.colorScheme.surfaceContainer
        ),
        border = null,
        leadingIcon = leadingIcon,
    )
}
