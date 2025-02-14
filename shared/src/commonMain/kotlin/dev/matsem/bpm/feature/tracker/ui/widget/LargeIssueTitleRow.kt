package dev.matsem.bpm.feature.tracker.ui.widget

import androidx.compose.foundation.MarqueeSpacing
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.unit.dp
import coil3.PlatformContext
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import dev.matsem.bpm.data.repo.model.Issue
import dev.matsem.bpm.data.repo.model.MockIssues
import dev.matsem.bpm.design.theme.BpmTheme
import dev.matsem.bpm.design.theme.Grid
import dev.matsem.bpm.design.tooling.HorizontalSpacer
import dev.matsem.bpm.design.tooling.Showcase
import dev.matsem.bpm.design.tooling.centeredVertically
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun LargeIssueTitleRow(
    issue: Issue,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    Row(
        modifier = modifier.padding(contentPadding), verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = ImageRequest.Builder(PlatformContext.INSTANCE)
                .data(issue.iconUrl)
                .crossfade(true)
                .build(),
            error = ColorPainter(BpmTheme.colorScheme.outline),
            contentDescription = null,
            modifier = Modifier.size(Grid.d6),
        )
        HorizontalSpacer(Grid.d2)
        Text(
            text = issue.key,
            color = BpmTheme.colorScheme.outline,
            style = BpmTheme.typography.bodyMedium.centeredVertically(),
            maxLines = 1,
        )
        HorizontalSpacer(Grid.d2)
        Text(
            text = issue.summary,
            color = BpmTheme.colorScheme.onSurface,
            style = BpmTheme.typography.bodyMedium.centeredVertically(),
            modifier = Modifier.basicMarquee(
                spacing = MarqueeSpacing.fractionOfContainer(0.1f),
                velocity = 50.dp
            ),
        )
    }
}

@Preview
@Composable
fun LargeIssueTitleRowPreview() {
    Showcase {
        LargeIssueTitleRow(issue = MockIssues.first(), modifier = Modifier.fillMaxWidth())
    }
}