package dev.matsem.bpm.feature.app.ui

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import bpm_tracker.shared.generated.resources.Res
import bpm_tracker.shared.generated.resources.new_version_banner_md
import com.mikepenz.markdown.m3.Markdown
import com.mikepenz.markdown.m3.markdownColor
import com.mikepenz.markdown.m3.markdownTypography
import dev.matsem.bpm.design.theme.BpmTheme
import dev.matsem.bpm.design.theme.Grid
import dev.matsem.bpm.tooling.Constants
import org.jetbrains.compose.resources.stringResource

@Composable
fun UpdateBanner(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
) {
    val transition = rememberInfiniteTransition(label = "gradient")
    val gradientOffset by transition.animateFloat(
        initialValue = 0f,
        targetValue = 2f,
        animationSpec = infiniteRepeatable(
            animation = tween(10_000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        )
    )

    CompositionLocalProvider(LocalContentColor provides Color.White) {
        Row(
            modifier = modifier
                .drawBehind {
                    val width = size.width
                    val gradient = Brush.horizontalGradient(
                        colors = listOf(Color(0xffFC466B), Color(0xff3F5EFB)),
                        tileMode = TileMode.Mirror,
                        startX = width * gradientOffset,
                        endX = width + width * gradientOffset
                    )
                    drawRect(gradient)
                }
                .padding(horizontal = BpmTheme.dimensions.horizontalContentPadding, vertical = Grid.d1),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Markdown(
                content = stringResource(Res.string.new_version_banner_md, Constants.LatestReleaseUrl),
                modifier = Modifier,
                typography = markdownTypography(
                    text = BpmTheme.typography.bodyMedium,
                    paragraph = BpmTheme.typography.bodyMedium,
                    link = BpmTheme.typography.bodyMedium.copy(
                        textDecoration = TextDecoration.Underline,
                        fontWeight = FontWeight.Bold
                    ),
                ),
                colors = markdownColor(text = LocalContentColor.current)
            )
            Icon(
                imageVector = Icons.Rounded.Close,
                contentDescription = null,
                modifier = Modifier
                    .size(Grid.d6)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = ripple(bounded = false),
                        role = Role.Button,
                        onClick = onDismiss
                    )
            )
        }
    }
}