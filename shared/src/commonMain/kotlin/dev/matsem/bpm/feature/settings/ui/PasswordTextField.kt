package dev.matsem.bpm.feature.settings.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import dev.matsem.bpm.design.input.AppTextField

@Composable
internal fun PasswordTextField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier
) {
    var passwordVisible by remember { mutableStateOf(false) }
    AppTextField(
        value = value,
        onValueChange = onValueChange,
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        singleLine = true,
        placeholder = "paste token here",
        trailingIcon = {
            PasswordRevealIcon(passwordVisible, { passwordVisible = !passwordVisible })
        },
        modifier = modifier
    )
}

@Composable
private fun PasswordRevealIcon(
    isPasswordVisible: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        modifier = modifier,
        onClick = onClick
    ) {
        Icon(
            when (isPasswordVisible) {
                true -> Icons.Rounded.VisibilityOff
                false -> Icons.Rounded.Visibility
            },
            contentDescription = when (isPasswordVisible) {
                true -> "Hide password"
                false -> "Show password"
            }
        )
    }
}