package dev.matsem.bpm.feature.app.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.LightMode
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.input.key.*
import androidx.compose.ui.platform.LocalFocusManager
import dev.matsem.bpm.design.sheet.GenericModalBottomSheet
import dev.matsem.bpm.design.sheet.SheetHeader
import dev.matsem.bpm.design.theme.BpmTheme
import dev.matsem.bpm.design.theme.Grid
import dev.matsem.bpm.design.tooling.HorizontalSpacer
import dev.matsem.bpm.feature.search.ui.SearchScreenUi
import dev.matsem.bpm.feature.settings.ui.SettingsScreenUi
import dev.matsem.bpm.feature.tracker.ui.TrackerScreenUi
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun AppUi() {
    val isSystemInDarkTheme = isSystemInDarkTheme() // Stores initial state of dark mode and stores in [darkMode] state.
    var darkMode by remember { mutableStateOf(isSystemInDarkTheme) }
    val focusManager = LocalFocusManager.current
    var isSettingsOpen by remember { mutableStateOf(false) }
    var isSearchOpen by remember { mutableStateOf(false) }

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
                BottomAppBar(
                    actions = {
                        IconButton(
                            onClick = { darkMode = !darkMode })
                        {
                            Icon(
                                if (darkMode) Icons.Rounded.LightMode else Icons.Rounded.DarkMode,
                                contentDescription = "Toggle dark mode",
                            )
                        }

                        IconButton(
                            onClick = { isSettingsOpen = true }
                        ) {
                            Icon(
                                Icons.Filled.Settings,
                                contentDescription = "Settings",
                            )
                        }
                        Text(
                            "tempo-desktop 0.0.1",
                            style = BpmTheme.typography.labelMedium,
                            modifier = Modifier.fillMaxWidth().padding(horizontal = Grid.d3)
                        )
                    },
                    floatingActionButton = {
                        ExtendedFloatingActionButton(
                            onClick = { isSearchOpen = true },
                            containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                            elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
                        ) {
                            Icon(Icons.Rounded.Search, contentDescription = "Search issues")
                            HorizontalSpacer(Grid.d1)
                            Text(text = "Search issues", style = BpmTheme.typography.bodyMedium)
                        }
                    }
                )
            }
        ) { contentPadding ->
            TrackerScreenUi(
                modifier = Modifier.fillMaxSize().padding(contentPadding)
            )
        }

        if (isSettingsOpen) {
            val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
            val coroutineScope = rememberCoroutineScope()
            GenericModalBottomSheet(
                onDismissRequest = { isSettingsOpen = false },
                sheetState = sheetState,
                header = {
                    SheetHeader(
                        title = "Settings",
                        onClose = {
                            coroutineScope.launch { sheetState.hide() }.invokeOnCompletion { isSettingsOpen = false }
                        }
                    )
                }
            ) {
                SettingsScreenUi()
            }
        }

        if (isSearchOpen) {
            val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
            val coroutineScope = rememberCoroutineScope()
            GenericModalBottomSheet(
                onDismissRequest = { isSearchOpen = false },
                sheetState = sheetState,
                header = {
                    SheetHeader(
                        title = "Pick an issue",
                        onClose = {
                            coroutineScope.launch { sheetState.hide() }.invokeOnCompletion { isSearchOpen = false }
                        }
                    )
                }
            ) {
                SearchScreenUi()
            }
        }
    }
}