package com.poulastaa.auth.ui.forgot_password

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalFocusManager
import com.poulastaa.auth.ui.forgot_password.screens.CompatHorizontalForgotPasswordScreen
import com.poulastaa.auth.ui.forgot_password.screens.CompatVerticalForgotPasswordScreen
import com.poulastaa.common.ui.LocalNavController
import com.poulastaa.common.ui.Screens.AuthScreens.ValidateOTP
import com.poulastaa.common.ui.components.ScreenSizeType
import com.poulastaa.common.ui.components.ScreenSizeWrapper
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ForgotPasswordScreen(
    email: String? = null,
    viewmodel: ForgotPasswordViewmodel = koinViewModel(),
) {
    val navController = LocalNavController.current
    val focusManager = LocalFocusManager.current
    val state by viewmodel.uiState.collectAsState()
    val isGetOtpEnabled = state.email.isError.not() && state.isMakingApiCall.not()

    LaunchedEffect(email) {
        email?.let { viewmodel.onAction(ForgotPasswordUiAction.OnEmailChange(it)) }
    }

    LaunchedEffect(viewmodel) {
        viewmodel.event.collect { event ->
            when (event) {
                is ForgotPasswordUiEvent.NavigateToOtp -> navController.navigate(
                    ValidateOTP(
                        email = event.email,
                    )
                )
            }
        }
    }

    ScreenSizeWrapper { screenSizeType ->
        when (screenSizeType) {
            ScreenSizeType.CompactVertical -> CompatVerticalForgotPasswordScreen(
                navController,
                state,
                viewmodel,
                focusManager,
                isGetOtpEnabled
            )

            ScreenSizeType.CompactHorizontal -> CompatHorizontalForgotPasswordScreen(
                navController,
                state,
                viewmodel,
                focusManager,
                isGetOtpEnabled
            )

            ScreenSizeType.LargeVertical -> TODO()
            ScreenSizeType.LargeHorizontal -> TODO()
        }
    }
}
