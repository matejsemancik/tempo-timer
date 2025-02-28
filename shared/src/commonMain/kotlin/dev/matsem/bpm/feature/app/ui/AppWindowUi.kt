package dev.matsem.bpm.feature.app.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.LightMode
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import bpm_tracker.shared.generated.resources.settings
import bpm_tracker.shared.generated.resources.timer
import bpm_tracker.shared.generated.resources.toggle_dark_mode
import dev.matsem.bpm.design.sheet.GenericModalBottomSheet
import dev.matsem.bpm.design.sheet.SheetHeader
import dev.matsem.bpm.design.theme.BpmTheme
import dev.matsem.bpm.design.theme.Grid
import dev.matsem.bpm.design.tooling.HorizontalSpacer
import dev.matsem.bpm.design.tooling.centeredVertically
import dev.matsem.bpm.feature.app.presentation.AppWindow
import dev.matsem.bpm.feature.app.presentation.AppWindowSheet
import dev.matsem.bpm.feature.commit.presentation.CommitArgs
import dev.matsem.bpm.feature.commit.ui.CommitScreenUi
import dev.matsem.bpm.feature.search.ui.SearchScreenUi
import dev.matsem.bpm.feature.settings.ui.SettingsScreenUi
import dev.matsem.bpm.feature.tracker.presentation.TrackerScreen
import dev.matsem.bpm.feature.tracker.ui.TrackerScreenUi
import dev.matsem.bpm.tooling.Platform
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

    val isSystemInDarkTheme = isSystemInDarkTheme() // Stores initial state of dark mode and stores in [darkMode] state.
    var darkMode by remember { mutableStateOf(isSystemInDarkTheme) }
    val focusManager = LocalFocusManager.current

    val trackerScreen: TrackerScreen = koinInject()
    val platform: Platform = koinInject()

    BpmTheme(isDark = darkMode) {
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
                        NewVersionRow(Modifier.fillMaxWidth(), onDismiss = actions::onUpdateBannerDismissClick)
                    }
                    BottomAppBar(
                        actions = {
                            IconButton(
                                onClick = { darkMode = !darkMode })
                            {
                                Icon(
                                    if (darkMode) Icons.Rounded.LightMode else Icons.Rounded.DarkMode,
                                    contentDescription = stringResource(Res.string.toggle_dark_mode),
                                )
                            }

                            IconButton(onClick = actions::onSettingsClick) {
                                Icon(
                                    Icons.Filled.Settings,
                                    contentDescription = stringResource(Res.string.settings),
                                )
                            }
                            Text(
                                "${stringResource(Res.string.app_name)} (${platform.getVersionString()})",
                                style = BpmTheme.typography.labelMedium,
                                color = BpmTheme.colorScheme.outline,
                                modifier = Modifier.fillMaxWidth().padding(horizontal = Grid.d3)
                            )
                        },
                        floatingActionButton = {
                            ExtendedFloatingActionButton(
                                onClick = actions::onSearchClick,
                                containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                                elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
                            ) {
                                Icon(Icons.Rounded.Add, contentDescription = stringResource(Res.string.new_timer))
                                HorizontalSpacer(Grid.d1)
                                Text(
                                    text = stringResource(Res.string.new_timer),
                                    style = BpmTheme.typography.bodyMedium.centeredVertically()
                                )
                            }
                        }
                    )
                }
            }
        ) { contentPadding ->
            TrackerScreenUi(
                modifier = Modifier.fillMaxSize().padding(contentPadding),
                screen = trackerScreen,
                openCommitDialog = actions::onOpenCommitDialog
            )
        }

        state.sheet?.let { appWindowSheet ->
            val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
            val coroutineScope = rememberCoroutineScope()
            GenericModalBottomSheet(
                onDismissRequest = actions::onDismissSheet,
                sheetState = sheetState,
                header = {
                    SheetHeader(
                        title = when (appWindowSheet) {
                            AppWindowSheet.Settings -> stringResource(Res.string.settings)
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
                    AppWindowSheet.Settings -> SettingsScreenUi()
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