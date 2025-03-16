package dev.matsem.bpm.design.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.matsem.bpm.design.theme.BpmTheme
import kotlinx.collections.immutable.ImmutableList

@Composable
fun BottomNavigationBar(
    items: ImmutableList<NavigationBarItem>,
    onClick: (NavigationBarItem) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .background(color = BpmTheme.colorScheme.surfaceContainerHighest, shape = RoundedCornerShape(100))
    ) {
        for (bottomNavItem in items) {
            IconButton(
                onClick = {
                    onClick(bottomNavItem)
                },
                colors = if (bottomNavItem.isSelected) IconButtonDefaults.filledIconButtonColors() else IconButtonDefaults.iconButtonColors()
            ) {
                Icon(
                    bottomNavItem.icon,
                    contentDescription = bottomNavItem.title
                )
            }
        }
    }
}
