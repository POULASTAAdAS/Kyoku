package com.poulastaa.auth.ui.reset_password

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.platform.LocalAutofillManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalHapticFeedback
import com.poulastaa.auth.ui.reset_password.screens.CompatVerticalResetPasswordScreen
import com.poulastaa.auth.viewmodels.reset_password.ResetPasswordUiAction
import com.poulastaa.auth.viewmodels.reset_password.ResetPasswordUiEvent
import com.poulastaa.auth.viewmodels.reset_password.ResetPasswordViewmodel
import com.poulastaa.common.ui.LocalNavController
import com.poulastaa.common.ui.components.ScreenSizeType
import com.poulastaa.common.ui.components.ScreenSizeWrapper
import com.poulastaa.common.viewmodels.Screens.AuthScreens.ForgotPassword
import com.poulastaa.common.viewmodels.Screens.AuthScreens.SignIn
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ResetPasswordScreen(
    token: String,
    email: String,
    viewmodel: ResetPasswordViewmodel = koinViewModel(),
) {
    val navController = LocalNavController.current
    val focusManager = LocalFocusManager.current
    val autofillManager = LocalAutofillManager.current
    val haptic = LocalHapticFeedback.current
    val state by viewmodel.uiState.collectAsState()

    LaunchedEffect(token, email) {
        viewmodel.onAction(ResetPasswordUiAction.OnTokenReceived(token))
        viewmodel.onAction(ResetPasswordUiAction.OnEmailReceived(email))
    }

    LaunchedEffect(viewmodel.event) {
        viewmodel.event.collect { event ->
            when (event) {
                // Saving goes through PasswordCredentialStore; dropping the autofill session avoids a second save prompt.
                ResetPasswordUiEvent.NavigateToSignIn -> {
                    autofillManager?.cancel()
                    navController.navigate(SignIn) {
                        popUpTo<SignIn> { inclusive = true }
                    }
                }

                ResetPasswordUiEvent.NavigateToForgotPassword -> navController.navigate(
                    ForgotPassword()
                ) {
                    popUpTo<ForgotPassword> { inclusive = true }
                }

                ResetPasswordUiEvent.NavigateBackToForgotPassword -> navController.popBackStack<ForgotPassword>(
                    inclusive = false
                )
            }
        }
    }

    ScreenSizeWrapper { screenSizeType ->
        when (screenSizeType) {
            ScreenSizeType.CompactVertical -> CompatVerticalResetPasswordScreen(
                state = state,
                viewmodel = viewmodel,
                focusManager = focusManager,
                haptic = haptic
            )

            ScreenSizeType.CompactHorizontal -> TODO()

            ScreenSizeType.LargeVertical -> TODO()
            ScreenSizeType.LargeHorizontal -> TODO()
        }
    }

    BackHandler {
        viewmodel.onAction(ResetPasswordUiAction.OnBackClick)
    }
}
