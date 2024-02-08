package com.poulastaa.kyoku.presentation.screen.auth.email.signup

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.autofill.AutofillNode
import androidx.compose.ui.autofill.AutofillType
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalAutofill
import androidx.compose.ui.platform.LocalAutofillTree
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.hilt.navigation.compose.hiltViewModel
import com.poulastaa.kyoku.data.model.auth.AuthUiEvent
import com.poulastaa.kyoku.data.model.auth.email.signup.EmailSignUpUiEvent

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun EmailSignUpScreen(
    viewModel: EmailSignUpViewModel = hiltViewModel(),
    navigate: (AuthUiEvent.Navigate) -> Unit
) {
    val context = LocalContext.current

    LaunchedEffect(key1 = viewModel.uiEvent) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is AuthUiEvent.Navigate -> navigate(event)
                is AuthUiEvent.ShowToast -> {
                    Toast.makeText(
                        context,
                        event.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    val autoFillEmail = AutofillNode(
        autofillTypes = listOf(AutofillType.EmailAddress),
        onFill = {
            viewModel.onEvent(event = EmailSignUpUiEvent.OnAutoFillEmail(it))
        }
    )

    val autoFillPassword = AutofillNode(
        autofillTypes = listOf(AutofillType.NewPassword),
        onFill = {
            viewModel.onEvent(event = EmailSignUpUiEvent.OnAutoFillPassword(it))
        }
    )

    val autoFillUsername = AutofillNode(
        autofillTypes = listOf(AutofillType.NewUsername),
        onFill = {
            viewModel.onEvent(event = EmailSignUpUiEvent.OnAutoFillUserName(it))
        }
    )

    val autoFill = LocalAutofill.current
    LocalAutofillTree.current += autoFillEmail
    LocalAutofillTree.current += autoFillPassword
    LocalAutofillTree.current += autoFillUsername

    val haptic = LocalHapticFeedback.current

    EmailSignUpScreenContent(
        email = viewModel.state.email,
        password = viewModel.state.password,
        userName = viewModel.state.userName,
        conformPassword = viewModel.state.conformPassword,

        onEmailChange = {
            viewModel.onEvent(event = EmailSignUpUiEvent.OnEmailEnter(it))
        },
        onPasswordChange = {
            viewModel.onEvent(event = EmailSignUpUiEvent.OnPasswordEnter(it))
        },
        onUserNameChange = {
            viewModel.onEvent(event = EmailSignUpUiEvent.OnUsernameEnter(it))
        },
        onConformPasswordChange = {
            viewModel.onEvent(event = EmailSignUpUiEvent.OnConformPasswordEnter(it))
        },

        emailSupportingText = viewModel.state.emailSupportingText,
        passwordSupportingText = viewModel.state.passwordSupportingText,
        conformPasswordSupportingText = viewModel.state.conformPasswordSupportingText,
        usernameSupportingText = viewModel.state.userNameSupportingText,

        isEmailError = viewModel.state.isEmailError,
        isPasswordError = viewModel.state.isPasswordError,
        isConformPasswordError = viewModel.state.isConformPasswordError,
        isUsernameError = viewModel.state.isUserNameError,

        autoFillEmail = autoFillEmail,
        autoFillPassword = autoFillPassword,
        autoFillUserName = autoFillUsername,
        autofill = autoFill,

        passwordVisibility = viewModel.state.isPasswordVisible,
        changePasswordVisibility = {
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            viewModel.onEvent(event = EmailSignUpUiEvent.OnPasswordVisibilityChange)
        },

        isResendVerificationMailPromptVisible = viewModel.state.isResendVerificationMailPromptVisible,
        sendVerificationMailTimer = viewModel.state.sendVerificationMailTimer,
        isResendMailEnabled = viewModel.state.isResendMailEnabled,
        resendButtonClicked = {
            viewModel.onEvent(event = EmailSignUpUiEvent.OnResendVerificationMailClick)
        },

        onLogInClick = {
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            viewModel.onEvent(event = EmailSignUpUiEvent.OnLogInClick)
        },
        onDone = {
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            viewModel.onEvent(event = EmailSignUpUiEvent.OnContinueClick)
        },
        isLoading = viewModel.state.isLoading
    )
}