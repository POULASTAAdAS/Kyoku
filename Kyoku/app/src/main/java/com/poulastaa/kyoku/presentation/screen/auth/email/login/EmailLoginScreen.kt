package com.poulastaa.kyoku.presentation.screen.auth.email.login

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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.hilt.navigation.compose.hiltViewModel
import com.poulastaa.kyoku.data.model.auth.AuthUiEvent
import com.poulastaa.kyoku.data.model.auth.email.login.EmailLoginUiEvent
import com.poulastaa.kyoku.data.model.auth.root.RootAuthUiEvent
import kotlinx.coroutines.flow.collect

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun EmailLoginScreen(
    viewModel: EmailLoginViewModel = hiltViewModel(),
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
            viewModel.onEvent(event = EmailLoginUiEvent.OnAutoFillEmail(it))
        }
    )

    val autoFillPassword = AutofillNode(
        autofillTypes = listOf(AutofillType.Password),
        onFill = {
            viewModel.onEvent(event = EmailLoginUiEvent.OnAutoFillPassword(it))
        }
    )

    val autoFill = LocalAutofill.current
    LocalAutofillTree.current += autoFillEmail
    LocalAutofillTree.current += autoFillPassword


    val haptic = LocalHapticFeedback.current

    EmailLogInScreenContent(
        email = viewModel.state.email,
        password = viewModel.state.password,
        autoFillEmail = autoFillEmail,
        autoFillPassword = autoFillPassword,
        autofill = autoFill,
        onEmailChange = {
            viewModel.onEvent(event = EmailLoginUiEvent.OnEmailEnter(it))
        },
        onPasswordChange = {
            viewModel.onEvent(event = EmailLoginUiEvent.OnPasswordEnter(it))
        },
        emailSupportingText = viewModel.state.emailSupportingText,
        passwordSupportingText = viewModel.state.passwordSupportingText,
        isEmailError = viewModel.state.isEmailError,
        isPasswordError = viewModel.state.isPasswordError,
        passwordVisibility = viewModel.state.isPasswordVisible,
        changePasswordVisibility = {
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            viewModel.onEvent(event = EmailLoginUiEvent.OnPasswordVisibilityChange)
        },
        onDone = {
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            viewModel.onEvent(event = EmailLoginUiEvent.OnContinueClick)
        },
        onForgotPassword = {
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            viewModel.onEvent(event = EmailLoginUiEvent.OnForgotPasswordClick)
        },
        onSignUpClick = {
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            viewModel.onEvent(event = EmailLoginUiEvent.OnSignUpClick)
        },
        isLoading = viewModel.state.isLoading
    )
}