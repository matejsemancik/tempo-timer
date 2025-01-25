package dev.matsem.bpm.feature.app.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.style.TextAlign
import dev.matsem.bpm.design.theme.BpmTheme
import dev.matsem.bpm.design.theme.Grid
import dev.matsem.bpm.feature.tracker.presentation.TrackerModel
import dev.matsem.bpm.feature.tracker.ui.TrackerScreenUi
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun AppUi() {
    val isSystemInDarkTheme = isSystemInDarkTheme() // Stores initial state of dark mode and stores in [darkMode] state.
    var darkMode by remember { mutableStateOf(isSystemInDarkTheme) }
    val focusManager = LocalFocusManager.current

    var isSettingsOpen by remember { mutableStateOf(false) }

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
                        IconButton(onClick = { /* do something */ }) {
                            Icon(Icons.Rounded.Search, contentDescription = "Search issues")
                        }
                        IconButton(onClick = {
                            darkMode = !darkMode
                        }) {
                            Icon(
                                if (darkMode) Icons.Rounded.LightMode else Icons.Rounded.DarkMode,
                                contentDescription = "Toggle dark mode",
                            )
                        }
                        IconButton(onClick = { isSettingsOpen = true }) {
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
                            onClick = { /* do something */ },
                            containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                            elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
                        ) {
                            Icon(Icons.Rounded.Add, "Add tracker")
                            Text("Timer")
                        }
                    }
                )
            }
        ) { contentPadding ->
            TrackerScreenUi(
                screen = TrackerModel,
                modifier = Modifier.fillMaxSize().padding(contentPadding)
            )
        }

        if (isSettingsOpen) {
            SettingsSheet(
                onDismissRequest = { isSettingsOpen = false }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsSheet(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val sheetState = rememberModalBottomSheetState()
    val coroutineScope = rememberCoroutineScope()
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        sheetState = sheetState,
        shape = BpmTheme.shapes.small,
        dragHandle = {
            SheetHeader(
                title = "Settings",
                modifier = Modifier.fillMaxWidth(),
                onClose = { coroutineScope.launch { sheetState.hide() }.invokeOnCompletion { onDismissRequest() } })
        }
    ) {
        Text("This is da sheet", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SheetHeader(
    modifier: Modifier = Modifier,
    title: String? = null,
    onClose: () -> Unit
) {
    TopAppBar(
        colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = Color.Transparent),
        modifier = modifier,
        title = { title?.let { Text(title) } },
        actions = {
            IconButton(
                onClick = onClose,
            ) {
                Icon(Icons.Rounded.Close, contentDescription = "Close")
            }
        }
    )
}