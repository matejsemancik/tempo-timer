package dev.matsem.bpm.feature.stats.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import bpm_tracker.shared.generated.resources.Res
import bpm_tracker.shared.generated.resources.stats_period
import bpm_tracker.shared.generated.resources.stats_weekly
import dev.matsem.bpm.data.repo.model.CurrentPeriodStats
import dev.matsem.bpm.data.repo.model.CurrentWeekStats
import dev.matsem.bpm.data.repo.model.Stats
import dev.matsem.bpm.design.theme.BpmTheme
import dev.matsem.bpm.design.theme.Grid
import dev.matsem.bpm.design.tooling.HorizontalSpacer
import dev.matsem.bpm.feature.stats.presentation.StatsWidget
import dev.matsem.bpm.feature.tracker.formatting.DurationFormatter.formatForTextInput
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

@Composable
fun StatsWidgetUi(
    widget: StatsWidget = koinInject(),
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    val state by widget.state.collectAsStateWithLifecycle()

    if (state.allStats.count() > 0) {
        var pageIndex by remember { mutableStateOf(0) }
        val stat = state.allStats[pageIndex]
        AnimatedContent(
            targetState = stat,
            modifier = modifier.clickable {
                val nextPage = (pageIndex + 1) % state.allStats.count()
                pageIndex = nextPage
            },
            transitionSpec = {
                slideInVertically { -it } togetherWith slideOutVertically { it }
            },
            contentAlignment = Alignment.TopStart
        ) { stat ->
            Row(
                modifier = Modifier.padding(contentPadding),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stat.title,
                    style = BpmTheme.typography.bodySmall,
                    color = BpmTheme.colorScheme.onSurface
                )
                HorizontalSpacer(Grid.d4)
                LinearProgressIndicator(
                    color = when (stat.percent) {
                        1f -> BpmTheme.customColorScheme.success
                        else -> ProgressIndicatorDefaults.linearColor
                    },
                    progress = { stat.percent },
                    modifier = Modifier.weight(1f).padding(end = Grid.d2)
                )
            }
        }
    }

//    VerticalPager(
//        horizontalAlignment = Alignment.Start,
//        modifier = modifier
//            .clickable {
//                val nextPage = (pagerState.currentPage + 1) % pagerState.pageCount
//                coroutineScope.launch {
//                    pagerState.animateScrollToPage(nextPage)
//                }
//            },
//        state = pagerState,
//        userScrollEnabled = false,
//        contentPadding = contentPadding,
//        pageSize = PageSize.Fill,
//        snapPosition = SnapPosition.Center,
//    ) { page ->
//        val stat = state.allStats[page]
//        Row(
//            verticalAlignment = Alignment.CenterVertically,
//        ) {
//            Text(
//                text = stat.title,
//                style = BpmTheme.typography.bodySmall,
//                color = BpmTheme.colorScheme.onSurface
//            )
//            HorizontalSpacer(Grid.d4)
//            LinearProgressIndicator(
//                color = when (stat.percent) {
//                    1f -> BpmTheme.customColorScheme.success
//                    else -> ProgressIndicatorDefaults.linearColor
//                },
//                progress = { stat.percent },
//                modifier = Modifier.weight(1f).padding(end = Grid.d2)
//            )
//        }
//    }
}

private val Stats.title: AnnotatedString
    @Composable
    get() {
        val title = when (this) {
            is CurrentPeriodStats -> stringResource(Res.string.stats_period)
            is CurrentWeekStats -> stringResource(Res.string.stats_weekly)
        }
        return buildAnnotatedString {
            append(title)
            if (trackedDuration > requiredDuration) {
                append(requiredDuration.formatForTextInput())
                append(' ')
                withStyle(SpanStyle(color = BpmTheme.customColorScheme.success)) {
                    append("+${overtime.formatForTextInput()}")
                }
            } else {
                append("${trackedDuration.formatForTextInput()} / ${requiredDuration.formatForTextInput()}")
            }
        }
    }

