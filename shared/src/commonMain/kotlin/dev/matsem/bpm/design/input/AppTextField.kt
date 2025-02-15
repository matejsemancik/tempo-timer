package dev.matsem.bpm.design.input

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import dev.matsem.bpm.design.theme.BpmTheme
import dev.matsem.bpm.design.theme.Grid
import dev.matsem.bpm.design.tooling.Showcase
import dev.matsem.bpm.design.tooling.VerticalSpacer
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTextField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    placeholder: String? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    prefix: String? = null,
    suffix: String? = null,
    supportingText: String? = null,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    interactionSource: MutableInteractionSource? = null,
) {
    val textStyle = BpmTheme.typography.bodyMedium
    val colors = OutlinedTextFieldDefaults.colors()

    @Suppress("NAME_SHADOWING")
    val interactionSource = interactionSource ?: remember { MutableInteractionSource() }

    val textColor =
        textStyle.color.takeOrElse {
            val focused = interactionSource.collectIsFocusedAsState().value
            when {
                !enabled -> colors.disabledTextColor
                isError -> colors.errorTextColor
                focused -> colors.focusedTextColor
                else -> colors.unfocusedTextColor
            }
        }
    val mergedTextStyle = textStyle.merge(TextStyle(color = textColor))

    CompositionLocalProvider(LocalTextSelectionColors provides colors.textSelectionColors) {
        BasicTextField(
            value = value,
            modifier = modifier,
            onValueChange = onValueChange,
            enabled = enabled,
            readOnly = readOnly,
            textStyle = mergedTextStyle,
            cursorBrush = SolidColor(if (isError) colors.errorCursorColor else colors.cursorColor),
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            interactionSource = interactionSource,
            singleLine = singleLine,
            maxLines = maxLines,
            minLines = minLines,
            decorationBox =
                @Composable { innerTextField ->
                    OutlinedTextFieldDefaults.DecorationBox(
                        value = value.text,
                        visualTransformation = visualTransformation,
                        innerTextField = innerTextField,
                        placeholder = placeholder?.let {
                            {
                                Text(placeholder, style = textStyle)
                            }
                        },
                        leadingIcon = leadingIcon,
                        trailingIcon = trailingIcon,
                        prefix = prefix?.let {
                            {
                                Text(prefix, style = textStyle)
                            }
                        },
                        suffix = suffix?.let {
                            {
                                Text(suffix, style = textStyle)
                            }
                        },
                        supportingText = supportingText?.let {
                            {
                                Text(supportingText, style = textStyle)
                            }
                        },
                        singleLine = singleLine,
                        enabled = enabled,
                        isError = isError,
                        interactionSource = interactionSource,
                        colors = colors,
                        contentPadding = PaddingValues(
                            horizontal = Grid.d4,
                            vertical = Grid.d2_5
                        ),
                        container = {
                            OutlinedTextFieldDefaults.Container(
                                enabled = enabled,
                                isError = isError,
                                interactionSource = interactionSource,
                                colors = colors,
                                shape = BpmTheme.shapes.small,
                                focusedBorderThickness = Grid.d0_5,
                                unfocusedBorderThickness = Grid.d0_25,
                            )
                        }
                    )
                }
        )
    }
}

@Preview
@Composable
fun AppTextFieldPreview() {
    Showcase {
        Column(verticalArrangement = Arrangement.spacedBy(Grid.d2)) {
            AppTextField(
                modifier = Modifier.fillMaxWidth().padding(horizontal = Grid.d3),
                value = TextFieldValue(""),
                onValueChange = {},
                placeholder = "Enter something"
            )

            AppTextField(
                modifier = Modifier.fillMaxWidth().padding(horizontal = Grid.d3),
                value = TextFieldValue("Text input"),
                onValueChange = {},
                placeholder = "Enter something"
            )

            AppTextField(
                modifier = Modifier.fillMaxWidth().padding(horizontal = Grid.d3),
                value = TextFieldValue("Some text that user entered"),
                onValueChange = {},
            )

            AppTextField(
                modifier = Modifier.fillMaxWidth().padding(horizontal = Grid.d3),
                value = TextFieldValue("instance"),
                onValueChange = {},
                prefix = "https://",
                suffix = ".atlassian.net"
            )

            AppTextField(
                modifier = Modifier.fillMaxWidth().padding(horizontal = Grid.d3),
                value = TextFieldValue("some input"),
                onValueChange = {},
                isError = true,
                supportingText = "No results"
            )

            VerticalSpacer(Grid.d1)
        }
    }
}