package com.poulastaa.auth.presentation.email.login.components

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
import androidx.compose.ui.platform.LocalAutofill
import androidx.compose.ui.platform.LocalAutofillTree
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.poulastaa.auth.presentation.email.components.AuthContinueButton
import com.poulastaa.auth.presentation.email.components.AuthTextField
import com.poulastaa.auth.presentation.email.login.EmailLogInUiState
import com.poulastaa.auth.presentation.email.login.EmailLoginUiEvent
import com.poulastaa.core.presentation.designsystem.AppLogo
import com.poulastaa.core.presentation.designsystem.AppThem
import com.poulastaa.core.presentation.designsystem.CheckIcon
import com.poulastaa.core.presentation.designsystem.EmailIcon
import com.poulastaa.core.presentation.designsystem.EyeCloseIcon
import com.poulastaa.core.presentation.designsystem.EyeOpenIcon
import com.poulastaa.core.presentation.designsystem.PasswordIcon
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.dimens

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun EmailLoginExpandedScreen(
    autoFillEmail: AutofillNode,
    autoFillPassword: AutofillNode,
    autoFill: Autofill?,
    state: EmailLogInUiState,
    onEvent: (EmailLoginUiEvent) -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val interactionSource = remember { MutableInteractionSource() }

    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(.45f)
                .fillMaxHeight(),
        ) {
            Image(
                painter = AppLogo,
                contentDescription = null,
                modifier = Modifier.aspectRatio(2.5f)
            )

            AnimatedVisibility(visible = state.isResendVerificationMailVisible) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Spacer(modifier = Modifier.height(MaterialTheme.dimens.large2))

                    Text(
                        text = stringResource(id = R.string.did_not_reveive__verification_mail),
                        color = MaterialTheme.colorScheme.background
                    )

                    Spacer(modifier = Modifier.height(MaterialTheme.dimens.small3))

                    AuthContinueButton(
                        text = if (state.canResendMailAgain) stringResource(id = R.string.send_again) else state.resendMailText,
                        enable = state.canResendMailAgain,
                        fontWeight = FontWeight.Normal,
                        fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                        modifier = Modifier
                            .fillMaxWidth(.8f)
                            .padding(MaterialTheme.dimens.small1),
                        isLoading = state.isResendMailLoading,
                        onClick = {
                            onEvent(EmailLoginUiEvent.OnResendMailClick)
                        }
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = MaterialTheme.dimens.medium1),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AuthTextField(
                modifier = Modifier
                    .fillMaxWidth()
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
                onValueChange = { onEvent(EmailLoginUiEvent.OnEmailChange(it)) },
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
                    ) else Unit
                },
                onDone = {
                    focusManager.moveFocus(FocusDirection.Down)
                }
            )

            Spacer(modifier = Modifier.height(MaterialTheme.dimens.small3))

            AuthTextField(
                modifier = Modifier
                    .fillMaxWidth()
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
                onValueChange = { onEvent(EmailLoginUiEvent.OnPasswordChange(it)) },
                label = stringResource(id = R.string.password),
                leadingIcon = {
                    Icon(
                        imageVector = PasswordIcon,
                        contentDescription = null
                    )
                },
                trailingIcon = {
                    IconButton(
                        onClick = {
                            onEvent(EmailLoginUiEvent.OnPasswordVisibilityToggle)
                        }
                    ) {
                        Icon(
                            imageVector = if (state.isPasswordVisible) EyeCloseIcon else EyeOpenIcon,
                            contentDescription = null
                        )
                    }
                },
                visualTransformation = if (state.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                onDone = {
                    focusManager.clearFocus()
                    onEvent(EmailLoginUiEvent.OnContinueClick)
                }
            )

            ForgotPassword(interactionSource = interactionSource) {
                onEvent(EmailLoginUiEvent.OnForgotPasswordClick)
            }

            Spacer(modifier = Modifier.height(MaterialTheme.dimens.large1))

            DontHaveAccount(interactionSource = interactionSource) {
                onEvent(EmailLoginUiEvent.OnEmailSignUpClick)
            }

            Spacer(modifier = Modifier.height(MaterialTheme.dimens.small3))

            AuthContinueButton(
                text = stringResource(id = R.string.continue_text),
                modifier = Modifier
                    .fillMaxWidth(.8f)
                    .padding(MaterialTheme.dimens.small1),
                isLoading = state.isMakingApiCall,
                onClick = {
                    onEvent(EmailLoginUiEvent.OnContinueClick)
                }
            )
        }
    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    widthDp = 840,
    heightDp = 360
)
@Preview(
    widthDp = 840,
    heightDp = 360
)
@Composable
private fun Preview() {
    val autoFillEmail = AutofillNode(
        autofillTypes = listOf(AutofillType.EmailAddress),
        onFill = {}
    )

    val autoFillPassword = AutofillNode(
        autofillTypes = listOf(AutofillType.Password),
        onFill = {}
    )

    val autoFill = LocalAutofill.current
    LocalAutofillTree.current += autoFillEmail
    LocalAutofillTree.current += autoFillPassword

    AppThem {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primaryContainer,
                            MaterialTheme.colorScheme.background,
                            MaterialTheme.colorScheme.background,
                        )
                    )
                )
                .padding(MaterialTheme.dimens.medium1)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            EmailLoginExpandedScreen(
                autoFillEmail = autoFillEmail,
                autoFillPassword = autoFillPassword,
                autoFill = autoFill,
                state = EmailLogInUiState()
            ) {}
        }
    }
}