package dev.matsem.bpm.design.sheet

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

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
                Icon(Icons.Rounded.Close, contentDescription = "Close")
            }
        }
    )
}