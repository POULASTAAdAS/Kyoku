package com.poulastaa.auth.ui.reset_password

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalHapticFeedback
import com.poulastaa.auth.ui.reset_password.screens.CompatVerticalResetPasswordScreen
import com.poulastaa.common.ui.components.ScreenSizeType
import com.poulastaa.common.ui.components.ScreenSizeWrapper

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ResetPasswordScreen() {
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPassword by remember { mutableStateOf("") }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current
    val isValidPassword = remember(password, confirmPassword) { password == confirmPassword }
    val haptic = LocalHapticFeedback.current

    ScreenSizeWrapper { screenSizeType ->
        when (screenSizeType) {
            ScreenSizeType.CompactVertical -> CompatVerticalResetPasswordScreen(
                password,
                onPasswordChange = { password = it },
                passwordVisible,
                onPasswordVisibilityToggle = { passwordVisible = passwordVisible.not() },
                confirmPassword,
                onConfirmPasswordChange = { confirmPassword = it },
                confirmPasswordVisible,
                onConfirmPasswordVisibilityToggle = {
                    confirmPasswordVisible = confirmPasswordVisible.not()
                },
                focusManager,
                haptic,
                isValidPassword
            )

            ScreenSizeType.CompactHorizontal -> TODO()

            ScreenSizeType.LargeVertical -> TODO()
            ScreenSizeType.LargeHorizontal -> TODO()
        }
    }

    BackHandler {}
}
