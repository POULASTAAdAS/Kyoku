package com.poulastaa.auth.presentation.singup.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.poulastaa.auth.presentation.components.AuthCard
import com.poulastaa.auth.presentation.components.AuthEmailTextField
import com.poulastaa.auth.presentation.components.AuthPasswordTextFiled
import com.poulastaa.auth.presentation.singup.EmailSingUpUiAction
import com.poulastaa.auth.presentation.singup.model.EmailSingUpUiState
import com.poulastaa.auth.presentation.singup.model.UsernameTextProp
import com.poulastaa.core.presentation.designsystem.AppTextField
import com.poulastaa.core.presentation.ui.AppTheme
import com.poulastaa.core.presentation.ui.CheckIcon
import com.poulastaa.core.presentation.ui.R
import com.poulastaa.core.presentation.ui.UserIcon

@Composable
internal fun SingUpCard(
    modifier: Modifier,
    heading: String? = null,
    state: EmailSingUpUiState,
    onAction: (EmailSingUpUiAction) -> Unit,
    focus: FocusManager,
    haptic: HapticFeedback,
) {
    val context = LocalContext.current

    AuthCard(
        modifier = modifier,
        heading = heading,
    ) {
        AppTextField(
            modifier = Modifier.fillMaxWidth(),
            data = state.username.prop,
            onValueChange = { onAction(EmailSingUpUiAction.OnUsernameChange(it)) },
            isValid = state.username.isValidUsername,
            trailingIcon = CheckIcon,
            placeHolder = stringResource(R.string.username),
            leadingIcon = UserIcon,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    focus.moveFocus(FocusDirection.Down)
                }
            )
        )

        AuthEmailTextField(
            email = state.email,
            onEmailChange = {
                onAction(EmailSingUpUiAction.OnEmailChange(it))
            },
            onMoveFocus = {
                focus.moveFocus(FocusDirection.Down)
            }
        )

        AuthPasswordTextFiled(
            password = state.password,
            onPasswordChange = { onAction(EmailSingUpUiAction.OnPasswordChange(it)) },
            onVisibilityToggle = {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                onAction(EmailSingUpUiAction.OnPasswordVisibilityToggle)
            },
            onSubmit = {
                focus.moveFocus(FocusDirection.Down)
            },
        )

        AuthPasswordTextFiled(
            password = state.conformPassword,
            heading = stringResource(R.string.conform_password),
            onPasswordChange = { onAction(EmailSingUpUiAction.OnConformPasswordChange(it)) },
            onVisibilityToggle = {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                onAction(EmailSingUpUiAction.OnConformPasswordVisibilityToggle)
            },
            onSubmit = {
                focus.clearFocus()
                onAction(EmailSingUpUiAction.OnSubmit(context))
            },
        )
    }
}

@PreviewLightDark
@Composable
private fun Preview() {
    AppTheme(isSystemInDarkTheme()) {
        SingUpCard(
            Modifier, heading = stringResource(R.string.sing_in),
            state = EmailSingUpUiState(
                username = UsernameTextProp(isValidUsername = isSystemInDarkTheme())
            ),
            onAction = {},
            focus = LocalFocusManager.current,
            haptic = LocalHapticFeedback.current
        )
    }
}