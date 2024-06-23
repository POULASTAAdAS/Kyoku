package com.poulastaa.auth.presentation.email.signup.components

import android.app.Activity
import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.Autofill
import androidx.compose.ui.autofill.AutofillNode
import androidx.compose.ui.autofill.AutofillType
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.poulastaa.auth.presentation.email.components.AuthContinueButton
import com.poulastaa.auth.presentation.email.components.AuthTextField
import com.poulastaa.auth.presentation.email.signup.EmailSignUpUiEvent
import com.poulastaa.auth.presentation.email.signup.EmailSignUpUiState
import com.poulastaa.core.presentation.designsystem.AppLogo
import com.poulastaa.core.presentation.designsystem.AppThem
import com.poulastaa.core.presentation.designsystem.CheckIcon
import com.poulastaa.core.presentation.designsystem.EmailIcon
import com.poulastaa.core.presentation.designsystem.EyeCloseIcon
import com.poulastaa.core.presentation.designsystem.EyeOpenIcon
import com.poulastaa.core.presentation.designsystem.PasswordIcon
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.UserIcon
import com.poulastaa.core.presentation.designsystem.dimens

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun EmailSignUpMedium(
    autoFillUserName: AutofillNode,
    autoFillEmail: AutofillNode,
    autoFillPassword: AutofillNode,
    autoFill: Autofill?,
    state: EmailSignUpUiState,
    onEvent: (EmailSignUpUiEvent) -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    Image(
        painter = AppLogo,
        contentDescription = null,
        modifier = Modifier.aspectRatio(1.8f)
    )

    Column(
        modifier = Modifier.fillMaxWidth(.8f),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AuthTextField(
            modifier = Modifier
                .onGloballyPositioned {
                    autoFillUserName.boundingBox = it.boundsInRoot()
                }
                .onFocusChanged {
                    autoFill?.run {
                        if (it.isFocused) requestAutofillForNode(autoFillUserName)
                        else cancelAutofillForNode(autoFillUserName)
                    }
                },
            text = state.userName,
            onValueChange = { onEvent(EmailSignUpUiEvent.OnUserNameChange(it)) },
            label = stringResource(id = R.string.username),
            keyboardType = KeyboardType.Text,
            leadingIcon = {
                Icon(
                    imageVector = UserIcon,
                    contentDescription = null
                )
            },
            trailingIcon = {
                if (state.isValidUserName) Icon(
                    imageVector = CheckIcon,
                    contentDescription = null
                )
            },
            onDone = {
                focusManager.moveFocus(FocusDirection.Down)
            }
        )

        Spacer(modifier = Modifier.height(MaterialTheme.dimens.small3))

        AuthTextField(
            modifier = Modifier
                .onGloballyPositioned {
                    autoFillEmail.boundingBox = it.boundsInRoot()
                }
                .onFocusChanged {
                    autoFill?.run {
                        if (it.isFocused) requestAutofillForNode(autoFillEmail)
                        else cancelAutofillForNode(autoFillEmail)
                    }
                },
            text = state.email,
            onValueChange = { onEvent(EmailSignUpUiEvent.OnEmailChange(it)) },
            label = stringResource(id = R.string.email),
            leadingIcon = {
                Icon(
                    imageVector = EmailIcon,
                    contentDescription = null
                )
            },
            trailingIcon = {
                if (state.isValidEmail) Icon(
                    imageVector = CheckIcon,
                    contentDescription = null
                )
            },
            onDone = {
                focusManager.moveFocus(FocusDirection.Down)
            }
        )

        Spacer(modifier = Modifier.height(MaterialTheme.dimens.small3))

        AuthTextField(
            modifier = Modifier
                .onGloballyPositioned {
                    autoFillPassword.boundingBox = it.boundsInRoot()
                }
                .onFocusChanged {
                    autoFill?.run {
                        if (it.isFocused) requestAutofillForNode(autoFillPassword)
                        else cancelAutofillForNode(autoFillPassword)
                    }
                },
            text = state.password,
            onValueChange = { onEvent(EmailSignUpUiEvent.OnPasswordChange(it)) },
            label = stringResource(id = R.string.password),
            keyboardType = KeyboardType.Password,
            leadingIcon = {
                Icon(
                    imageVector = PasswordIcon,
                    contentDescription = null
                )
            },
            trailingIcon = {
                IconButton(onClick = { onEvent(EmailSignUpUiEvent.OnPasswordVisibilityToggle) }) {
                    Icon(
                        imageVector = if (state.isPasswordVisible) EyeCloseIcon else EyeOpenIcon,
                        contentDescription = null
                    )
                }
            },
            visualTransformation = if (state.isPasswordVisible) VisualTransformation.None
            else PasswordVisualTransformation(),
            onDone = {
                focusManager.moveFocus(FocusDirection.Down)
            }
        )

        Spacer(modifier = Modifier.height(MaterialTheme.dimens.small3))

        AuthTextField(
            modifier = Modifier
                .onGloballyPositioned {
                    autoFillPassword.boundingBox = it.boundsInRoot()
                }
                .onFocusChanged {
                    autoFill?.run {
                        if (it.isFocused) requestAutofillForNode(autoFillPassword)
                        else cancelAutofillForNode(autoFillPassword)
                    }
                },
            text = state.confirmPassword,
            onValueChange = { onEvent(EmailSignUpUiEvent.OnConfirmPasswordChange(it)) },
            label = stringResource(id = R.string.conform_password),
            keyboardType = KeyboardType.Password,
            leadingIcon = {
                Icon(
                    imageVector = PasswordIcon,
                    contentDescription = null
                )
            },
            visualTransformation = PasswordVisualTransformation(),
            onDone = {
                focusManager.clearFocus()
                onEvent(EmailSignUpUiEvent.OnContinueClick(context as Activity))
            }
        )


        Spacer(modifier = Modifier.height(MaterialTheme.dimens.large2))

        AlreadyHaveAccount {
            onEvent(EmailSignUpUiEvent.OnEmailLogInClick)
        }

        Spacer(modifier = Modifier.height(MaterialTheme.dimens.small3))

        AuthContinueButton(
            modifier = Modifier
                .fillMaxWidth(.6f)
                .padding(MaterialTheme.dimens.small1),
            text = stringResource(id = R.string.continue_text),
            isLoading = state.isMakingApiCall,
            fontWeight = FontWeight.Medium,
            fontSize = MaterialTheme.typography.titleMedium.fontSize,
            onClick = {
                onEvent(EmailSignUpUiEvent.OnContinueClick(context as Activity))
            }
        )
    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Preview(
    device = "spec:width=800dp,height=1280dp,dpi=480",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Preview(
    device = "spec:width=800dp,height=1280dp,dpi=480"
)
@Composable
private fun Preview() {
    val autoFillEmail = AutofillNode(
        autofillTypes = listOf(AutofillType.EmailAddress),
        onFill = {}
    )

    AppThem {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primaryContainer,
                            MaterialTheme.colorScheme.background,
                            MaterialTheme.colorScheme.background,
                        )
                    )
                )
                .padding(MaterialTheme.dimens.medium1),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            EmailSignUpMedium(
                autoFillUserName = autoFillEmail,
                autoFillEmail = autoFillEmail,
                autoFillPassword = autoFillEmail,
                autoFill = null,
                state = EmailSignUpUiState()
            ) {

            }
        }
    }
}