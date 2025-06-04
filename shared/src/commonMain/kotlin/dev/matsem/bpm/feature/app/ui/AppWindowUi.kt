package dev.matsem.bpm.feature.app.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalFocusManager
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import bpm_tracker.shared.generated.resources.Res
import bpm_tracker.shared.generated.resources.app_name
import bpm_tracker.shared.generated.resources.new_timer
import bpm_tracker.shared.generated.resources.pick_issue
import bpm_tracker.shared.generated.resources.timer
import dev.matsem.bpm.data.repo.model.AppThemeMode
import dev.matsem.bpm.design.navigation.BottomNavigationBar
import dev.matsem.bpm.design.sheet.GenericModalBottomSheet
import dev.matsem.bpm.design.sheet.SheetHeader
import dev.matsem.bpm.design.theme.BpmTheme
import dev.matsem.bpm.design.theme.Grid
import dev.matsem.bpm.design.tooling.HorizontalSpacer
import dev.matsem.bpm.design.tooling.centeredVertically
import dev.matsem.bpm.feature.app.presentation.AppWindow
import dev.matsem.bpm.feature.app.presentation.AppWindowContent
import dev.matsem.bpm.feature.app.presentation.AppWindowSheet
import dev.matsem.bpm.feature.commit.presentation.CommitArgs
import dev.matsem.bpm.feature.commit.ui.CommitScreenUi
import dev.matsem.bpm.feature.logbook.ui.LogbookScreenUi
import dev.matsem.bpm.feature.search.ui.SearchScreenUi
import dev.matsem.bpm.feature.settings.ui.SettingsScreenUi
import dev.matsem.bpm.feature.stats.ui.StatsWidgetUi
import dev.matsem.bpm.feature.tracker.presentation.TrackerScreen
import dev.matsem.bpm.feature.tracker.ui.TrackerScreenUi
import dev.matsem.bpm.tooling.Platform
import io.github.kdroidfilter.platformtools.darkmodedetector.isSystemInDarkMode
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun AppWindowUi(
    window: AppWindow = koinInject(),
) {
    val state by window.state.collectAsStateWithLifecycle()
    val actions = window.actions

    val focusManager = LocalFocusManager.current

    val trackerScreen: TrackerScreen = koinInject()
    val platform: Platform = koinInject()

    val isDarkTheme = when (state.themeMode) {
        AppThemeMode.SYSTEM -> isSystemInDarkMode()
        AppThemeMode.LIGHT -> false
        AppThemeMode.DARK -> true
    }

    BpmTheme(isDark = isDarkTheme) {
        Scaffold(
            modifier = Modifier.onPreviewKeyEvent { keyEvent ->
                // Let user use arrow keys on the main screen in addition to TAB key for focusing elements
                if (keyEvent.type != KeyEventType.KeyDown) {
                    return@onPreviewKeyEvent false
                }

                return@onPreviewKeyEvent when (keyEvent.key) {
                    Key.DirectionDown -> {
                        focusManager.moveFocus(FocusDirection.Down)
                        true
                    }

                    Key.DirectionUp -> {
                        focusManager.moveFocus(FocusDirection.Up)
                        true
                    }

                    Key.DirectionLeft -> {
                        focusManager.moveFocus(FocusDirection.Left)
                        true
                    }

                    Key.DirectionRight -> {
                        focusManager.moveFocus(FocusDirection.Right)
                        true
                    }

                    else -> false
                }
            },
            bottomBar = {
                Column {
                    AnimatedVisibility(
                        visible = state.newVersionBannerVisible,
                        enter = slideInVertically { it },
                        exit = slideOutVertically { it }
                    ) {
                        UpdateBanner(
                            modifier = Modifier.fillMaxWidth(),
                            appVersion = state.latestAppVersion,
                            onDismiss = actions::onUpdateBannerDismissClick
                        )
                    }
                    AnimatedVisibility(
                        visible = state.isStatsVisible,
                        enter = slideInVertically { it } + fadeIn(animationSpec = spring(stiffness = Spring.StiffnessMedium)),
                        exit = fadeOut(animationSpec = spring(stiffness = Spring.StiffnessHigh))
                    ) {
                        StatsWidgetUi(
                            contentPadding = PaddingValues(
                                start = BpmTheme.dimensions.horizontalContentPadding,
                                end = BpmTheme.dimensions.horizontalContentPadding,
                                top = Grid.d2,
                                bottom = Grid.d1
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(BpmTheme.colorScheme.surfaceContainer)
                        )
                    }
                    BottomAppBar(
                        actions = {
                            HorizontalSpacer(BpmTheme.dimensions.horizontalContentPadding)
                            BottomNavigationBar(
                                items = state.navigationItems,
                                onClick = actions::onNavigationBarClick
                            )
                            Text(
                                "${stringResource(Res.string.app_name)} (${platform.getVersionString()})",
                                style = BpmTheme.typography.labelMedium,
                                color = BpmTheme.colorScheme.outline,
                                modifier = Modifier.fillMaxWidth().padding(horizontal = Grid.d3)
                            )
                        },
                        floatingActionButton = {
                            AnimatedVisibility(
                                visible = state.isFabVisible,
                                enter = fadeIn(animationSpec = spring(stiffness = Spring.StiffnessMedium)),
                                exit = fadeOut(animationSpec = spring(stiffness = Spring.StiffnessHigh))
                            ) {
                                ExtendedFloatingActionButton(
                                    onClick = actions::onNewTimerClick,
                                    containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                                    elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
                                ) {
                                    Icon(
                                        Icons.Rounded.Add,
                                        contentDescription = stringResource(Res.string.new_timer)
                                    )
                                    HorizontalSpacer(Grid.d1)
                                    Text(
                                        text = stringResource(Res.string.new_timer),
                                        style = BpmTheme.typography.bodyMedium.centeredVertically()
                                    )
                                }
                            }
                        }
                    )
                }
            }
        ) { contentPadding ->
            AnimatedContent(
                targetState = state.navigationState.content,
                transitionSpec = {
                    fun order(content: AppWindowContent) = when (content) {
                        AppWindowContent.Timer -> 0
                        AppWindowContent.Logbook -> 1
                        AppWindowContent.Settings -> 2
                    }

                    val slideTowards = if (order(targetState) > order(initialState)) {
                        AnimatedContentTransitionScope.SlideDirection.Start
                    } else {
                        AnimatedContentTransitionScope.SlideDirection.End
                    }

                    val enterTransition = fadeIn(
                        animationSpec = tween(220, delayMillis = 90)
                    ) + slideIntoContainer(towards = slideTowards) { it / 15 }

                    val exitTransition = fadeOut(
                        animationSpec = tween(90)
                    ) + slideOutOfContainer(towards = slideTowards) { it / 15 }

                    enterTransition togetherWith exitTransition
                },
            ) { content ->
                when (content) {
                    AppWindowContent.Timer -> TrackerScreenUi(
                        modifier = Modifier.fillMaxSize().padding(contentPadding),
                        screen = trackerScreen,
                        openCommitDialog = actions::onOpenCommitDialog
                    )

                    AppWindowContent.Logbook -> LogbookScreenUi(
                        modifier = Modifier.fillMaxSize().padding(contentPadding),
                    )

                    AppWindowContent.Settings -> SettingsScreenUi(
                        modifier = Modifier.fillMaxSize().padding(contentPadding),
                    )
                }
            }
        }

        state.navigationState.sheet?.let { appWindowSheet ->
            val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
            val coroutineScope = rememberCoroutineScope()
            GenericModalBottomSheet(
                onDismissRequest = actions::onDismissSheet,
                sheetState = sheetState,
                header = {
                    SheetHeader(
                        title = when (appWindowSheet) {
                            AppWindowSheet.Search -> stringResource(Res.string.pick_issue)
                            is AppWindowSheet.CommitDialog -> stringResource(Res.string.timer)
                        },
                        onClose = {
                            coroutineScope.launch { sheetState.hide() }.invokeOnCompletion { actions.onDismissSheet() }
                        }
                    )
                }
            ) {
                when (appWindowSheet) {
                    AppWindowSheet.Search -> SearchScreenUi(
                        onIssueSelected = { issue ->
                            actions.onDismissSheet()
                            trackerScreen.actions.onNewTimer(issue)
                        }
                    )

                    is AppWindowSheet.CommitDialog -> CommitScreenUi(
                        screen = koinInject(
                            parameters = { parametersOf(CommitArgs(appWindowSheet.timer)) }
                        ),
                        onDismissRequest = actions::onDismissSheet
                    )
                }
            }
        }
    }
}
