package com.poulastaa.core.presentation.designsystem.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.poulastaa.core.presentation.designsystem.AppThem

@Composable
fun AppTextField(
    modifier: Modifier = Modifier,
    text: String,
    onValueChange: (String) -> Unit,
    label: String,
    isErr: Boolean = false,
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = MaterialTheme.colorScheme.primary,
        unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(.5f),

        focusedLabelColor = MaterialTheme.colorScheme.primary,
        unfocusedLabelColor = MaterialTheme.colorScheme.onSurface.copy(.5f),

        cursorColor = MaterialTheme.colorScheme.secondary,

        focusedContainerColor = MaterialTheme.colorScheme.onSurface.copy(.05f),
        unfocusedContainerColor = MaterialTheme.colorScheme.onSurface.copy(.05f),

        focusedLeadingIconColor = MaterialTheme.colorScheme.primary,
        unfocusedLeadingIconColor = MaterialTheme.colorScheme.onSurface.copy(.5f),

        focusedTrailingIconColor = MaterialTheme.colorScheme.primary,
        unfocusedTrailingIconColor = MaterialTheme.colorScheme.onSurface.copy(.5f),
    ),
    keyboardType: KeyboardType = KeyboardType.Text,
    supportingText: String = "",
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    onDone: () -> Unit,
) {

    OutlinedTextField(
        modifier = modifier,
        value = text,
        onValueChange = onValueChange,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        label = { Text(text = label) },
        shape = MaterialTheme.shapes.medium,
        singleLine = true,
        isError = isErr,
        supportingText = {
            if (isErr) Text(text = supportingText) else Unit
        },
        colors = colors,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                onDone()
            }
        )
    )
}

@Preview(
    uiMode = UI_MODE_NIGHT_YES
)
@Composable
private fun Preview() {
    AppThem {
        AppTextField(
            text = "",
            onValueChange = {},
            label = "Email"
        ) {}
    }
}