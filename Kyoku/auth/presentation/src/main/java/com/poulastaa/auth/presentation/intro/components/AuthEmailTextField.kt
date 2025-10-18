package com.poulastaa.auth.presentation.intro.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.poulastaa.auth.presentation.intro.model.EmailTextProp
import com.poulastaa.core.domain.Email
import com.poulastaa.core.presentation.ui.AlternateEmailIcon
import com.poulastaa.core.presentation.ui.CheckIcon
import com.poulastaa.core.presentation.ui.R


@Composable
internal fun AuthEmailTextField(
    modifier: Modifier = Modifier.fillMaxWidth(),
    email: EmailTextProp,
    onEmailChange: (email: Email) -> Unit,
    onMoveFocus: () -> Unit,
) {
    OutlinedTextField(
        value = email.prop.value,
        onValueChange = onEmailChange,
        modifier = modifier,
        shape = MaterialTheme.shapes.extraLarge,
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,

            cursorColor = MaterialTheme.colorScheme.tertiary,
            unfocusedTextColor = MaterialTheme.colorScheme.tertiary,
            unfocusedLabelColor = MaterialTheme.colorScheme.tertiary,
            unfocusedBorderColor = MaterialTheme.colorScheme.tertiary,
            unfocusedPlaceholderColor = MaterialTheme.colorScheme.tertiary,
            unfocusedLeadingIconColor = MaterialTheme.colorScheme.tertiary,
            unfocusedTrailingIconColor = MaterialTheme.colorScheme.tertiary,

            focusedTextColor = MaterialTheme.colorScheme.secondary,
            focusedLabelColor = MaterialTheme.colorScheme.secondary,
            focusedBorderColor = MaterialTheme.colorScheme.secondary,
            focusedPlaceholderColor = MaterialTheme.colorScheme.secondary,
            focusedLeadingIconColor = MaterialTheme.colorScheme.secondary,
            focusedTrailingIconColor = MaterialTheme.colorScheme.secondary,

            focusedSupportingTextColor = MaterialTheme.colorScheme.error,
            unfocusedSupportingTextColor = MaterialTheme.colorScheme.error,

            errorTextColor = MaterialTheme.colorScheme.error,
            errorLabelColor = MaterialTheme.colorScheme.error,
            errorCursorColor = MaterialTheme.colorScheme.error,
            errorBorderColor = MaterialTheme.colorScheme.error,
            errorSupportingTextColor = MaterialTheme.colorScheme.error,
            errorPlaceholderColor = MaterialTheme.colorScheme.error,
            errorLeadingIconColor = MaterialTheme.colorScheme.error,
            errorTrailingIconColor = MaterialTheme.colorScheme.error
        ),
        placeholder = {
            Text(
                text = stringResource(R.string.email)
            )
        },
        leadingIcon = {
            Icon(
                imageVector = AlternateEmailIcon,
                contentDescription = stringResource(R.string.email)
            )
        },
        trailingIcon = email.isValidEmail.takeIf { it }?.let { _ ->
            {
                Icon(
                    imageVector = CheckIcon,
                    contentDescription = stringResource(R.string.email)
                )
            }
        },
        isError = email.prop.isErr,
        supportingText = { Text(text = email.prop.errText.asString()) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(
            onNext = { onMoveFocus() }
        )
    )
}
