package com.poulastaa.auth.ui.otp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.platform.LocalFocusManager
import com.poulastaa.auth.ui.otp.screens.CompatVerticalOtpScreen
import com.poulastaa.auth.viewmodels.otp.OtpUiAction
import com.poulastaa.auth.viewmodels.otp.OtpUiEvent
import com.poulastaa.auth.viewmodels.otp.OtpViewmodel
import com.poulastaa.auth.viewmodels.utils.OTP_LENGTH
import com.poulastaa.common.ui.LocalNavController
import com.poulastaa.common.ui.components.ScreenSizeType
import com.poulastaa.common.ui.components.ScreenSizeWrapper
import com.poulastaa.common.viewmodels.Screens.AuthScreens.ResetPassword
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun OtpScreen(
    email: String,
    viewmodel: OtpViewmodel = koinViewModel(),
) {
    val navController = LocalNavController.current
    val focusManager = LocalFocusManager.current
    val state by viewmodel.uiState.collectAsState()
    val isValidOTP = state.otp.value.length == OTP_LENGTH

    LaunchedEffect(email) {
        viewmodel.onAction(OtpUiAction.OnEmailReceived(email))
    }

    LaunchedEffect(viewmodel.event) {
        viewmodel.event.collect { event ->
            when (event) {
                is OtpUiEvent.NavigateToResetPassword -> navController.navigate(
                    route = ResetPassword(
                        token = event.token,
                    )
                )
            }
        }
    }

    ScreenSizeWrapper { screenSizeType ->
        when (screenSizeType) {
            ScreenSizeType.CompactVertical -> CompatVerticalOtpScreen(
                email = email,
                otp = state.otp.value,
                isOTPError = state.otp.isError,
                isValidating = state.isMakingApiCall,
                isValidOTP = isValidOTP,
                focusManager = focusManager,
                onOtpChange = { viewmodel.onAction(OtpUiAction.OnOtpChange(it)) },
                onValidateClick = { viewmodel.onAction(OtpUiAction.ValidateOtp) }
            )

            ScreenSizeType.CompactHorizontal -> TODO()

            ScreenSizeType.LargeVertical -> TODO()
            ScreenSizeType.LargeHorizontal -> TODO()
        }
    }

    BackHandler {}
}
