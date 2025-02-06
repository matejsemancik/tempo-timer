package dev.matsem.bpm.design.layout


import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.FocusInteraction
import androidx.compose.foundation.interaction.HoverInteraction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.material3.Card
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.semantics.Role
import dev.matsem.bpm.design.theme.BpmTheme
import dev.matsem.bpm.design.theme.Grid

@Composable
fun ClickableDesignCard(
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    var hasFocusBorder by remember { mutableStateOf(false) }
    val isPressed by interactionSource.collectIsPressedAsState()

    LaunchedEffect(Unit) {
        interactionSource.interactions.collect { interaction ->
            when (interaction) {
                is FocusInteraction.Focus -> hasFocusBorder = true
                is FocusInteraction.Unfocus -> hasFocusBorder = false
                is HoverInteraction.Enter -> hasFocusBorder = true
                is HoverInteraction.Exit -> hasFocusBorder = false
            }
        }
    }

    val hoverBorder = when (hasFocusBorder) {
        true -> BorderStroke(Grid.d0_25, color = BpmTheme.colorScheme.primary)
        false -> null
    }

    val pressScale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessHigh, dampingRatio = Spring.DampingRatioNoBouncy)
    )

    Card(
        modifier = modifier
            .clickable(
                onClick = onClick,
                interactionSource = interactionSource,
                indication = null,
                role = Role.Button
            )
            .scale(scale = pressScale),
        shape = BpmTheme.shapes.small,
        border = hoverBorder,
    ) {
        content()
    }
}