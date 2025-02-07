package dev.matsem.bpm.feature.tracker.ui.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import dev.matsem.bpm.design.theme.BpmTheme

@Composable
fun IssueTypeIcon(
    url: String,
    modifier: Modifier = Modifier
) {
    AsyncImage(
        model = url,
        modifier = modifier,
        contentDescription = null
    )
}