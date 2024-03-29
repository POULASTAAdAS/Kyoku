package com.poulastaa.kyoku.presentation.screen.auth.root

import android.app.Activity
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
import com.poulastaa.kyoku.data.model.screens.auth.root.RootUiEvent
import com.poulastaa.kyoku.presentation.screen.auth.root.google.StartActivityForResult
import com.poulastaa.kyoku.presentation.screen.auth.root.google.singIn

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RootAuthScreen(
    viewModel: RootAuthViewModel = hiltViewModel(),
    navigateToEmailAuth: (UiEvent.Navigate) -> Unit
) {
    val context = LocalContext.current

    LaunchedEffect(key1 = viewModel.uiEvent) {
        viewModel.uiEvent.collect { uiEvent ->
            when (uiEvent) {
                is UiEvent.Navigate -> navigateToEmailAuth.invoke(uiEvent)

                is UiEvent.ShowToast -> {
                    Toast.makeText(
                        context,
                        uiEvent.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
                else -> Unit
            }
        }
    }

    StartActivityForResult(
        key = viewModel.state.googleAuthLoading,
        onResultReceived = {
            viewModel.onEvent(
                RootUiEvent.SendGoogleAuthApiRequest(
                    token = it,
                    activity = context as Activity
                )
            )
        },
        onDialogDismissed = {
            viewModel.onEvent(RootUiEvent.OnAuthCanceled)
        },
        launcher = { activityLauncher ->
            if (viewModel.state.googleAuthLoading) {
                singIn(
                    activity = context as Activity,
                    launchActivityResult = {
                        viewModel.onEvent(RootUiEvent.OnAuthCanceled)
                        activityLauncher.launch(it)
                    },
                    accountNotFound = {
                        viewModel.onEvent(RootUiEvent.OnAuthCanceled)
                        viewModel.onEvent(RootUiEvent.NoGoogleAccountFound)
                    },
                    somethingWentWrong = {
                        viewModel.onEvent(RootUiEvent.OnAuthCanceled)
                        viewModel.onEvent(RootUiEvent.SomeErrorOccurredOnAuth)
                    }
                )
            }
        }
    )


    val autoFillEmail = AutofillNode(
        autofillTypes = listOf(AutofillType.EmailAddress),
        onFill = {
            viewModel.onEvent(RootUiEvent.OnAutoFillPasskeyEmail(it))
        }
    )

    val autoFill = LocalAutofill.current
    LocalAutofillTree.current += autoFillEmail

    val haptic = LocalHapticFeedback.current
    val focusManager = LocalFocusManager.current

    RootAuthScreenContent(
        email = viewModel.state.passkeyEmail,
        onValueChange = {
            viewModel.onEvent(RootUiEvent.OnPasskeyEmailEnter(it))
        },
        autoFillEmail = autoFillEmail,
        autoFill = autoFill,
        isError = viewModel.state.isPasskeyEmailError,
        supportingText = viewModel.state.passkeyEmailSupportingText,
        passkeySignUpClicked = {
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            focusManager.clearFocus()
            viewModel.onEvent(RootUiEvent.OnPasskeyAuthClick(context as Activity))
        },
        emailSignUpClicked = {
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            viewModel.onEvent(RootUiEvent.OnEmailAuthClick)
        },
        googleSignupClicked = {
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            viewModel.onEvent(RootUiEvent.OnGoogleAuthClick)
        },
        passkeyContinueLoading = viewModel.state.passkeyAuthLoading,
        googleAuthLoading = viewModel.state.googleAuthLoading,
        emailAuthLoading = viewModel.state.emailAuthLoading
    )
}