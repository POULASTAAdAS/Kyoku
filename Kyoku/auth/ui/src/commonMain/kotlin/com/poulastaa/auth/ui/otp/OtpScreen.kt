package com.poulastaa.auth.ui.otp

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.platform.LocalFocusManager
import com.poulastaa.auth.ui.otp.screens.CompatVerticalOtpScreen
import com.poulastaa.common.ui.components.ScreenSizeType
import com.poulastaa.common.ui.components.ScreenSizeWrapper

internal const val OTP_LENGTH = 5

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun OtpScreen() {
    val email by remember { mutableStateOf("poulastaadas2@gmail.com") }
    var otp by remember { mutableStateOf("") }
    val isOTPError by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val otpFontStyle = MaterialTheme.typography.headlineLarge
    var isValidating by remember { mutableStateOf(false) }
    val isValidOTP by remember(otp) { mutableStateOf(otp.length == OTP_LENGTH) }

    LaunchedEffect(isValidOTP) {
        if (isValidOTP) isValidating = true
    }

    ScreenSizeWrapper { screenSizeType ->
        when (screenSizeType) {
            ScreenSizeType.CompactVertical -> CompatVerticalOtpScreen(
                email,
                otp,
                isOTPError,
                isValidating,
                isValidOTP,
                focusManager,
                onOtpChange = { otp = it },
                onValidateClick = { isValidating = true }
            )

            ScreenSizeType.CompactHorizontal -> TODO()

            ScreenSizeType.LargeVertical -> TODO()
            ScreenSizeType.LargeHorizontal -> TODO()
        }
    }

    BackHandler {}
}
