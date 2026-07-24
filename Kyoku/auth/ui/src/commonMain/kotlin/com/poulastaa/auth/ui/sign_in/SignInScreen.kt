package com.poulastaa.auth.ui.sign_in

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalHapticFeedback
import com.poulastaa.auth.ui.components.GoogleAuthResult
import com.poulastaa.auth.ui.components.GoogleAuthWrapper
import com.poulastaa.auth.ui.sign_in.screens.CompatHorizontalSignInScreen
import com.poulastaa.auth.ui.sign_in.screens.CompatVerticalSignInScreen
import com.poulastaa.auth.ui.sign_in.screens.ExpandedSignInScreen
import com.poulastaa.common.ui.LocalNavController
import com.poulastaa.common.ui.Screens
import com.poulastaa.common.ui.Screens.AuthScreens.ForgotPassword
import com.poulastaa.common.ui.Screens.AuthScreens.SignUp
import com.poulastaa.common.ui.components.ScreenSizeType
import com.poulastaa.common.ui.components.ScreenSizeWrapper
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SignInScreen(
    viewmodel: SignInViewmodel = koinViewModel(),
) {
    val focusManager = LocalFocusManager.current
    val haptic = LocalHapticFeedback.current
    val navController = LocalNavController.current
    val googleAuthWrapper = koinInject<GoogleAuthWrapper>()
    val state by viewmodel.uiState.collectAsState()

    DisposableEffect(googleAuthWrapper, viewmodel) {
        googleAuthWrapper.onResult = { result ->
            when (result) {
                is GoogleAuthResult.Success -> viewmodel.onAction(
                    SignInUiAction.OnGoogleAuthSuccess(
                        result.token
                    )
                )

                GoogleAuthResult.Canceled,
                is GoogleAuthResult.Error,
                    -> viewmodel.onAction(SignInUiAction.OnGoogleAuthCanceled)
            }
        }

        onDispose { googleAuthWrapper.onResult = null }
    }

    LaunchedEffect(state.isGoogleAuthInProgress, googleAuthWrapper) {
        if (state.isGoogleAuthInProgress) googleAuthWrapper.startGoogleAuth()
    }

    LaunchedEffect(viewmodel) {
        viewmodel.event.collect { event ->
            when (event) {
                is SignInUiEvent.NavigateToForgotPassword -> navController.navigate(
                    ForgotPassword(event.email)
                )

                SignInUiEvent.NavigateToSignUp -> navController.navigate(SignUp)

                SignInUiEvent.NavigateToHome -> navController.navigate(Screens.MainScreens.Home)
                SignInUiEvent.NavigateToImportPlaylist -> navController.navigate(Screens.SetupScreens.ImportPlaylist)
            }
        }
    }

    ScreenSizeWrapper { screenSizeType ->
        when (screenSizeType) {
            ScreenSizeType.CompactVertical -> CompatVerticalSignInScreen(
                state,
                viewmodel,
                focusManager,
                haptic
            )

            ScreenSizeType.CompactHorizontal -> CompatHorizontalSignInScreen(
                state,
                viewmodel,
                focusManager,
                haptic
            )

            ScreenSizeType.LargeVertical,
            ScreenSizeType.LargeHorizontal,
                -> ExpandedSignInScreen(
                    state,
                    viewmodel,
                    focusManager,
                    haptic
                )
        }
    }
}
