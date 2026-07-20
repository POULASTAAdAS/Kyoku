package com.poulastaa.auth.ui.sign_up

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalHapticFeedback
import com.poulastaa.auth.ui.components.GoogleAuthResult
import com.poulastaa.auth.ui.components.GoogleAuthWrapper
import com.poulastaa.auth.ui.sign_up.screens.CompatHorizontalSignUpScreen
import com.poulastaa.auth.ui.sign_up.screens.CompatVerticalSignUpScreen
import com.poulastaa.common.ui.LocalNavController
import com.poulastaa.common.ui.Screens
import com.poulastaa.common.ui.components.ScreenSizeType
import com.poulastaa.common.ui.components.ScreenSizeWrapper
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SingUpScreen(
    viewmodel: SignUpViewmodel = koinViewModel(),
) {
    val navController = LocalNavController.current
    val focusManager = LocalFocusManager.current
    val haptic = LocalHapticFeedback.current
    val googleAuthWrapper = koinInject<GoogleAuthWrapper>()
    val state by viewmodel.uiState.collectAsState()

    DisposableEffect(googleAuthWrapper, viewmodel) {
        googleAuthWrapper.onResult = { result ->
            when (result) {
                is GoogleAuthResult.Success -> viewmodel.onAction(
                    SignUpUiAction.OnGoogleTokenReceived(
                        result.token
                    )
                )

                GoogleAuthResult.Canceled,
                is GoogleAuthResult.Error,
                    -> viewmodel.onAction(SignUpUiAction.OnGoogleAuthCanceled)
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
                SignUpUiEvent.NavigateToLogIn -> navController.popBackStack()
                SignUpUiEvent.NavigateToHome -> navController.navigate(Screens.MainScreens.Home)
                SignUpUiEvent.NavigateToImportPlaylist -> navController.navigate(Screens.SetupScreens.ImportPlaylist)
            }
        }
    }

    ScreenSizeWrapper { screenSizeType ->
        when (screenSizeType) {
            ScreenSizeType.CompactVertical -> CompatVerticalSignUpScreen(
                navController,
                state,
                viewmodel,
                focusManager,
                haptic
            )

            ScreenSizeType.CompactHorizontal -> CompatHorizontalSignUpScreen(
                state,
                viewmodel,
                focusManager,
                haptic
            )

            ScreenSizeType.LargeVertical -> TODO()
            ScreenSizeType.LargeHorizontal -> TODO()
        }
    }
}
