package com.poulastaa.core.presentation.designsystem.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.poulastaa.core.presentation.designsystem.AppThem
import com.poulastaa.core.presentation.designsystem.CheckIcon
import com.poulastaa.core.presentation.designsystem.CloseIcon
import com.poulastaa.core.presentation.designsystem.EmailAlternateIcon

@Composable
fun AppTextField(
    modifier: Modifier = Modifier,
    text: String,
    label: String,
    onValueChange: (String) -> Unit,
    maxLines: Int = 1,
    leadingPainter: Painter? = null,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    supportingText: String? = null,
    isError: Boolean = false,
    isClearButtonEnabled: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    onClearClick: () -> Unit = {},
) {
    OutlinedTextField(
        value = text,
        onValueChange = onValueChange,
        modifier = modifier,
        leadingIcon = if (leadingPainter != null) {
            {
                Image(
                    painter = leadingPainter,
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(30.dp)
                )
            }
        } else if (leadingIcon != null) {
            {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = null
                )
            }
        } else null,
        trailingIcon = if (isClearButtonEnabled && text.isNotEmpty()) {
            {
                Icon(
                    imageVector = CloseIcon,
                    contentDescription = null,
                    modifier = Modifier.clickable(
                        indication = null,
                        interactionSource = null,
                        onClick = onClearClick
                    )
                )
            }
        } else trailingIcon?.let {
            {
                Icon(
                    imageVector = it,
                    contentDescription = null,
                )
            }
        },
        supportingText = supportingText?.let {
            {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        },
        isError = isError,
        maxLines = maxLines,
        label = {
            Text(
                text = label,
                fontWeight = FontWeight.Light,
            )
        },
        shape = MaterialTheme.shapes.extraLarge,
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = MaterialTheme.colorScheme.primary.copy(.7f),
            unfocusedLabelColor = MaterialTheme.colorScheme.primary.copy(.7f),
            unfocusedLeadingIconColor = MaterialTheme.colorScheme.primary.copy(.7f),
            unfocusedTrailingIconColor = MaterialTheme.colorScheme.primary.copy(.7f),

            focusedBorderColor = MaterialTheme.colorScheme.primary,
            focusedLabelColor = MaterialTheme.colorScheme.primary,
            focusedLeadingIconColor = MaterialTheme.colorScheme.primary,
            focusedTrailingIconColor = MaterialTheme.colorScheme.primary,

            focusedTextColor = MaterialTheme.colorScheme.primary,
            unfocusedTextColor = MaterialTheme.colorScheme.primary,
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = imeAction
        ),
        keyboardActions = keyboardActions
    )
}

@PreviewLightDark
@Composable
private fun Preview() {
    AppThem {
        Surface {
            AppTextField(
                text = "",
                onValueChange = { },
                label = "Email",
                leadingIcon = EmailAlternateIcon,
                trailingIcon = CheckIcon
            )
        }
    }
}