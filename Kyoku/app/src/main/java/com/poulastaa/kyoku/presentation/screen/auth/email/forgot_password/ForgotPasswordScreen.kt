package com.poulastaa.kyoku.presentation.screen.auth.email.forgot_password

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
import com.poulastaa.kyoku.data.model.screens.auth.UiEvent
import com.poulastaa.kyoku.data.model.screens.auth.email.forgot_password.ForgotPasswordUiEvent

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ForgotPasswordScreen(
    viewModel: ForgotPasswordViewModel = hiltViewModel(),
    navigateBack: () -> Unit
) {
    val context = LocalContext.current

    LaunchedEffect(key1 = viewModel.uiEvent) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.Navigate -> navigateBack.invoke()
                is UiEvent.ShowToast -> {
                    Toast.makeText(
                        context,
                        event.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
                else -> Unit
            }
        }
    }

    val autoFillEmail = AutofillNode(
        autofillTypes = listOf(AutofillType.EmailAddress),
        onFill = {
            viewModel.onEvent(ForgotPasswordUiEvent.OnAutoFillEmailClick(it))
        }
    )

    val autoFill = LocalAutofill.current
    LocalAutofillTree.current += autoFillEmail

    val haptic = LocalHapticFeedback.current
    val focusManager = LocalFocusManager.current

    ForgotPasswordScreenContent(
        email = viewModel.state.email,
        onValueChange = {
            viewModel.onEvent(ForgotPasswordUiEvent.OnEmailEnter(it))
        },
        isError = viewModel.state.isEmailError,
        isLoading = viewModel.state.isLoading,
        autoFillEmail = autoFillEmail,
        autofill = autoFill,
        emailSupportingText = viewModel.state.emailSupportingText,
        onNavigateBack = {
            viewModel.onEvent(event = ForgotPasswordUiEvent.OnNavigateBackClicked)
        },
        onDone = {
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            focusManager.clearFocus()

            viewModel.onEvent(event = ForgotPasswordUiEvent.OnGetEmailClick)
        },
        isEnabled = viewModel.state.isEnabled,
        enableTimer = viewModel.state.enableTimer,
        emailSendText = viewModel.state.emailSendText
    )
}