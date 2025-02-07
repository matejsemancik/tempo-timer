package dev.matsem.bpm.feature.search.ui.widget

import androidx.compose.animation.*
import androidx.compose.animation.core.snap
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ripple
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.matsem.bpm.data.model.domain.Issue
import dev.matsem.bpm.data.model.domain.MockIssues
import dev.matsem.bpm.design.theme.BpmTheme
import dev.matsem.bpm.design.theme.Grid
import dev.matsem.bpm.design.theme.favourite
import dev.matsem.bpm.design.tooling.Showcase
import dev.matsem.bpm.feature.tracker.ui.widget.IssueTitleRow
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun SearchResultRow(
    issue: Issue,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    var isFavourite by remember { mutableStateOf(false) }

    val toggleFavourite = fun() {
        isFavourite = !isFavourite
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clickable(interactionSource = interactionSource, indication = ripple(), onClick = {})
            .padding(contentPadding)
    ) {
        IssueTitleRow(
            issue = issue,
            modifier = Modifier
                .padding(vertical = Grid.d3)
                .weight(1f)
        )

        if (isHovered || isFavourite) {
            IconButton(
                onClick = toggleFavourite,
            ) {
                AnimatedContent(
                    targetState = isFavourite,
                    transitionSpec = {
                        if (targetState) {
                            scaleIn(initialScale = 1.5f) togetherWith ExitTransition.None
                        } else {
                            EnterTransition.None togetherWith ExitTransition.None
                        }
                    }
                ) { isFavourite ->
                    if (isFavourite) {
                        Icon(Icons.Rounded.Star, contentDescription = null, tint = BpmTheme.colorScheme.favourite)
                    } else {
                        Icon(Icons.Rounded.StarBorder, contentDescription = null, tint = BpmTheme.colorScheme.favourite)
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun SearchResultRowPreview() {
    Showcase {
        SearchResultRow(
            issue = MockIssues.first(),
            contentPadding = PaddingValues(0.dp),
            modifier = Modifier.fillMaxWidth()
        )
    }
}