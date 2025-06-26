package dev.matsem.bpm.feature.stats.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import bpm_tracker.shared.generated.resources.Res
import bpm_tracker.shared.generated.resources.stats_ahead
import bpm_tracker.shared.generated.resources.stats_behind
import bpm_tracker.shared.generated.resources.stats_period
import bpm_tracker.shared.generated.resources.stats_today
import bpm_tracker.shared.generated.resources.stats_weekly
import dev.matsem.bpm.data.repo.model.WorkStats
import dev.matsem.bpm.data.repo.model.WorkStats.Type
import dev.matsem.bpm.design.theme.BpmTheme
import dev.matsem.bpm.design.theme.Grid
import dev.matsem.bpm.design.tooling.VerticalSpacer
import dev.matsem.bpm.feature.stats.presentation.StatsWidget
import dev.matsem.bpm.feature.tracker.formatting.DurationFormatter.formatForWorkStats
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import kotlin.time.Duration

@Composable
fun StatsWidgetUi(
    widget: StatsWidget = koinInject(),
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    val state by widget.state.collectAsStateWithLifecycle()
    val actions = widget.actions

    LifecycleResumeEffect(Unit) {
        actions.onViewResumed()
        onPauseOrDispose {}
    }

    val workStats = state.displayedStats
    if (workStats != null) {
        AnimatedContent(
            targetState = workStats,
            contentKey = { it.type },
            modifier = modifier.clickable { actions.onClick() },
            transitionSpec = {
                slideInVertically { it } togetherWith slideOutVertically { -it }
            },
            contentAlignment = Alignment.TopStart
        ) { workStats ->
            val progress by animateFloatAsState(
                workStats.percent, animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessVeryLow
                )
            )
            Column(Modifier.padding(contentPadding)) {
                Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = workStats.title,
                        style = BpmTheme.typography.bodySmall,
                        color = BpmTheme.colorScheme.onSurface,
                    )
                    workStats.aheadOrBehindText?.let {
                        Text(
                            text = it,
                            style = BpmTheme.typography.bodySmall,
                            color = BpmTheme.colorScheme.onSurface,
                        )
                    }
                }
                VerticalSpacer(Grid.d1)
                LinearProgressIndicator(
                    color = when (workStats.percent) {
                        1f -> BpmTheme.customColorScheme.success
                        else -> ProgressIndicatorDefaults.linearColor
                    },
                    progress = { progress },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

private val WorkStats.aheadOrBehindText: AnnotatedString?
    @Composable
    get() {
        if (type != Type.CurrentPeriod || trackingDelta == Duration.ZERO) {
            return null
        }

        val (textRes, duration) = when {
            trackingDelta > Duration.ZERO -> Res.string.stats_ahead to trackingDelta
            else -> Res.string.stats_behind to -trackingDelta
        }

        val text = stringResource(textRes, duration.formatForWorkStats())

        return buildAnnotatedString {
            append(text)
        }
    }

private val WorkStats.title: AnnotatedString
    @Composable
    get() {
        val title = when (this.type) {
            WorkStats.Type.Today -> stringResource(Res.string.stats_today)
            WorkStats.Type.ThisWeek -> stringResource(Res.string.stats_weekly)
            WorkStats.Type.CurrentPeriod -> stringResource(Res.string.stats_period)
        }
        return buildAnnotatedString {
            append(title)
            if (trackedDuration > requiredDuration) {
                withStyle(SpanStyle(color = BpmTheme.customColorScheme.success)) {
                    append(trackedDuration.formatForWorkStats())
                }
            } else {
                append(trackedDuration.formatForWorkStats())
            }
            append(" / ${requiredDuration.formatForWorkStats()}")
        }
    }
