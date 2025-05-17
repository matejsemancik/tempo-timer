package dev.matsem.bpm.design.layout

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.matsem.bpm.design.theme.BpmTheme
import dev.matsem.bpm.design.theme.Grid
import dev.matsem.bpm.design.tooling.VerticalSpacer

/**
 * A reusable section component that displays a title with content underneath.
 * This pattern is used across multiple screens in the app.
 *
 * @param title The title of the section, typically includes an emoji followed by text
 * @param modifier The modifier to be applied to the section
 * @param content The content to be displayed under the title
 */
@Composable
fun SectionWithTitle(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(modifier = modifier) {
        Text(
            text = title,
            style = BpmTheme.typography.titleMedium,
            modifier = Modifier.fillMaxWidth().padding(horizontal = BpmTheme.dimensions.horizontalContentPadding)
        )
        VerticalSpacer(Grid.d2)
        content()
    }
}
