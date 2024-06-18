package com.poulastaa.auth.presentation.email.login.components

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.poulastaa.core.presentation.designsystem.AppThem
import com.poulastaa.core.presentation.designsystem.dimens
import com.poulastaa.core.presentation.ui.model.TextHolder

@Composable
fun AuthTextField(
    modifier: Modifier = Modifier,
    text: TextHolder,
    onValueChange: (String) -> Unit,
    label: String,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardType: KeyboardType = KeyboardType.Email,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    onDone: () -> Unit,
) {
    OutlinedTextField(
        modifier = modifier.fillMaxWidth(),
        value = text.data,
        onValueChange = onValueChange,
        singleLine = true,
        shape = MaterialTheme.shapes.medium,
        label = {
            Text(text = label)
        },
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        isError = text.isErr,
        supportingText = {
            if (text.isErr) Text(text = text.errText.asString()) else Unit
        },
        visualTransformation = visualTransformation,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                onDone()
            }
        ),
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = Color.Transparent,
            focusedContainerColor = Color.Transparent,

            unfocusedIndicatorColor = MaterialTheme.colorScheme.primaryContainer,
            focusedIndicatorColor = MaterialTheme.colorScheme.secondaryContainer,

            unfocusedLabelColor = MaterialTheme.colorScheme.primaryContainer,
            focusedLabelColor = MaterialTheme.colorScheme.secondaryContainer,

            unfocusedLeadingIconColor = MaterialTheme.colorScheme.primaryContainer,
            focusedLeadingIconColor = MaterialTheme.colorScheme.secondaryContainer,

            unfocusedTrailingIconColor = MaterialTheme.colorScheme.primaryContainer,
            focusedTrailingIconColor = MaterialTheme.colorScheme.secondaryContainer,

            errorIndicatorColor = MaterialTheme.colorScheme.error,
            errorTrailingIconColor = MaterialTheme.colorScheme.error,
            errorLeadingIconColor = MaterialTheme.colorScheme.error,
            errorSupportingTextColor = MaterialTheme.colorScheme.error,
            errorTextColor = MaterialTheme.colorScheme.error,

            unfocusedTextColor = MaterialTheme.colorScheme.primaryContainer,
            focusedTextColor = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.background
            else MaterialTheme.colorScheme.onBackground,
        )
    )
}

@Preview
@Composable
private fun Preview() {
    AppThem {
        Column(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.background)
                .padding(MaterialTheme.dimens.medium1)
        ) {
            AuthTextField(
                modifier = Modifier.fillMaxWidth(),
                text = TextHolder(),
                label = "Email",
                onValueChange = {}
            ) {

            }
        }
    }
}