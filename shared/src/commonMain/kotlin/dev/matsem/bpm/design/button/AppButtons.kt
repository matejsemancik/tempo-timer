package dev.matsem.bpm.design.button

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.matsem.bpm.design.theme.BpmTheme
import dev.matsem.bpm.design.theme.Grid
import dev.matsem.bpm.design.tooling.Showcase
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun AppButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false
) {
    Button(
        onClick = onClick,
        shape = BpmTheme.shapes.small,
        enabled = enabled && !isLoading,
        modifier = modifier
    ) {
        ButtonContent(text, isLoading)
    }
}

@Composable
fun AppOutlinedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false
) {
    OutlinedButton(
        onClick = onClick,
        shape = BpmTheme.shapes.small,
        enabled = enabled && !isLoading,
        modifier = modifier
    ) {
        ButtonContent(text, isLoading)
    }
}

@Composable
private fun ButtonContent(text: String, isLoading: Boolean) {
    if (isLoading) {
        CircularProgressIndicator(modifier = Modifier.size(Grid.d5), strokeWidth = Grid.d0_5)
    } else {
        Text(text = text)
    }
}

@Preview
@Composable
private fun AppButtonPreview() {
    Showcase {
        AppButton(
            text = "asdf",
            isLoading = true,
            onClick = {},
            modifier = Modifier.padding(Grid.d3)
        )
    }
}