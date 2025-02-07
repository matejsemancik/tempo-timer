package dev.matsem.bpm.design.input

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation

@Composable
fun DesignTextField(
    value: String,
    onValueChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String? = null,
    isPassword: Boolean = false,
    prefix: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
) {
    var passwordVisible by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChanged,
        modifier = modifier,
        prefix = prefix,
        suffix = suffix,
        label = label?.let { { Text(label) } },
        placeholder = placeholder?.let {
            {
                Text(placeholder)
            }
        },
        visualTransformation = if (!passwordVisible && isPassword) {
            PasswordVisualTransformation()
        } else {
            VisualTransformation.None
        },
        singleLine = true,
        trailingIcon = if (!isPassword) null else {
            {
                IconButton(
                    onClick = {
                        passwordVisible = !passwordVisible
                    }
                ) {
                    Icon(
                        when (passwordVisible) {
                            true -> Icons.Rounded.VisibilityOff
                            false -> Icons.Rounded.Visibility
                        },
                        contentDescription = when (passwordVisible) {
                            true -> "Hide password"
                            false -> "Show password"
                        }
                    )
                }
            }
        },
    )
}