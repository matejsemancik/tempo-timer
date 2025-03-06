package dev.matsem.bpm.feature.tracker.ui.widget

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.PlatformContext
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import dev.matsem.bpm.data.repo.model.Issue
import dev.matsem.bpm.design.theme.BpmTheme
import dev.matsem.bpm.design.theme.Grid
import dev.matsem.bpm.design.tooling.HorizontalSpacer
import dev.matsem.bpm.design.tooling.VerticalSpacer
import dev.matsem.bpm.design.tooling.centeredVertically

@Composable
fun IssueTitleRow(
    issue: Issue,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    Row(modifier = modifier.padding(contentPadding), verticalAlignment = Alignment.CenterVertically) {
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
        Column {
            Text(
                text = buildAnnotatedString {
                    append(issue.key)
                    if (issue.browseUrl.isNotBlank()) {
                        addLink(
                            url = LinkAnnotation.Url(
                                url = issue.browseUrl,
                                styles = TextLinkStyles(
                                    hoveredStyle = SpanStyle(textDecoration = TextDecoration.Underline),
                                ),
                            ),
                            start = 0,
                            end = length
                        )
                    }
                },
                color = BpmTheme.colorScheme.outline,
                style = BpmTheme.typography.labelSmall.centeredVertically(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            VerticalSpacer(Grid.d0_5)
            Text(
                text = issue.summary,
                color = BpmTheme.colorScheme.onSurface,
                style = BpmTheme.typography.bodySmall.centeredVertically(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}
