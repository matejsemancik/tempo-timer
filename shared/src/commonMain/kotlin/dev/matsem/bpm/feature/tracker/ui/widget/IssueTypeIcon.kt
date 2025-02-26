package dev.matsem.bpm.feature.tracker.ui.widget

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import coil3.compose.AsyncImage

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