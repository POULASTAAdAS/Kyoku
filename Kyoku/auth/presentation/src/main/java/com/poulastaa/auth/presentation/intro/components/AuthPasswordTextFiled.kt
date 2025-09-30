package com.poulastaa.auth.presentation.intro.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.poulastaa.auth.presentation.intro.IntroUiAction
import com.poulastaa.auth.presentation.intro.model.PasswordTextProp
import com.poulastaa.core.presentation.ui.EyeCloseIcon
import com.poulastaa.core.presentation.ui.EyeOpenIcon
import com.poulastaa.core.presentation.ui.PasswordIcon
import com.poulastaa.core.presentation.ui.R

@Composable
internal fun AuthPasswordTextFiled(
    password: PasswordTextProp,
    onAction: (IntroUiAction) -> Unit,
    haptic: HapticFeedback,
    focus: FocusManager,
) {
    OutlinedTextField(
        value = password.prop.value,
        onValueChange = {
            onAction(IntroUiAction.OnPasswordChange(it))
        },
        modifier = Modifier.fillMaxWidth(),
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
            errorBorderColor = MaterialTheme.colorScheme.error
        ),
        placeholder = {
            Text(
                text = stringResource(R.string.password)
            )
        },
        leadingIcon = {
            Icon(
                imageVector = PasswordIcon,
                contentDescription = stringResource(R.string.password)
            )
        },
        trailingIcon = {
            IconButton(
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    onAction(IntroUiAction.ObPasswordVisibilityToggle)
                }
            ) {
                AnimatedContent(
                    password.isPasswordVisible
                ) {
                    when (it) {
                        true -> Icon(
                            imageVector = EyeOpenIcon,
                            contentDescription = stringResource(R.string.password)
                        )

                        false -> Icon(
                            imageVector = EyeCloseIcon,
                            contentDescription = stringResource(R.string.password)
                        )
                    }
                }
            }
        },
        isError = password.prop.isErr,
        supportingText = {
            Text(
                text = password.prop.errText.asString()
            )
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                focus.clearFocus()
                onAction(IntroUiAction.OnEmailSubmit)
            }
        ),
        visualTransformation = if (password.isPasswordVisible) VisualTransformation.None
        else PasswordVisualTransformation()
    )
}