package dev.matsem.bpm.design.sheet

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import bpm_tracker.shared.generated.resources.Res
import bpm_tracker.shared.generated.resources.close
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SheetHeader(
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
    title: String? = null,
) {
    TopAppBar(
        colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = Color.Transparent),
        modifier = modifier,
        title = { title?.let { Text(title) } },
        actions = {
            IconButton(
                onClick = onClose,
            ) {
                Icon(
                    Icons.Rounded.Close, 
                    contentDescription = stringResource(Res.string.close)
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SheetHeader(
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit,
) {
    TopAppBar(
        colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = Color.Transparent),
        modifier = modifier,
        title = title,
        actions = {
            IconButton(
                onClick = onClose,
            ) {
                Icon(
                    Icons.Rounded.Close, 
                    contentDescription = stringResource(Res.string.close)
                )
            }
        }
    )
}