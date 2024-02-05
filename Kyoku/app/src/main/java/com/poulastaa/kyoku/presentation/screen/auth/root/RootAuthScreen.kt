package com.poulastaa.kyoku.presentation.screen.auth.root

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
import com.poulastaa.kyoku.data.model.auth.root.RootAuthUiEvent

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RootAuthScreen(
    viewModel: RootAuthViewModel = hiltViewModel(),
    navigateToEmailAuth: (AuthUiEvent.Navigate) -> Unit
) {
    val context = LocalContext.current

    LaunchedEffect(key1 = viewModel.uiEvent) {
        viewModel.uiEvent.collect { uiEvent ->
            when (uiEvent) {
                is AuthUiEvent.Navigate -> navigateToEmailAuth.invoke(uiEvent)

                is AuthUiEvent.ShowToast -> {
                    Toast.makeText(
                        context,
                        uiEvent.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    val autoFillEmail = AutofillNode(
        autofillTypes = listOf(AutofillType.EmailAddress),
        onFill = {
            viewModel.onEvent(RootAuthUiEvent.OnAuthFillPasskeyEmail(it))
        }
    )

    val autoFill = LocalAutofill.current
    LocalAutofillTree.current += autoFillEmail

    val haptic = LocalHapticFeedback.current
    val focusManager = LocalFocusManager.current

    RootAuthScreenContent(
        email = viewModel.state.passkeyEmail,
        onValueChange = {
            viewModel.onEvent(RootAuthUiEvent.OnPasskeyEmailEnter(it))
        },
        autoFillEmail = autoFillEmail,
        autoFill = autoFill,
        isError = viewModel.state.isPasskeyEmailError,
        supportingText = viewModel.state.passkeyEmailSupportingText,
        passkeySignUpClicked = {
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            focusManager.clearFocus()
            viewModel.onEvent(RootAuthUiEvent.OnPasskeyAuthClick)
        },
        emailSignUpClicked = {
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            viewModel.onEvent(RootAuthUiEvent.OnEmailAuthClick)
        },
        googleSignupClicked = {
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            viewModel.onEvent(RootAuthUiEvent.OnGoogleAuthClick)
        },
        passkeyContinueLoading = viewModel.state.passkeyAuthLoading,
        googleAuthLoading = viewModel.state.googleAuthLoading,
        emailAuthLoading = viewModel.state.emailAuthLoading
    )
}