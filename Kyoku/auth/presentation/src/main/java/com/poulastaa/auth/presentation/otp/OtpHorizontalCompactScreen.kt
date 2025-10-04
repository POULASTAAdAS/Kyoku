package com.poulastaa.auth.presentation.otp

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.keyframes
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalHapticFeedback
import com.poulastaa.core.presentation.designsystem.TextProp
import com.poulastaa.core.presentation.designsystem.UiText
import com.poulastaa.core.presentation.ui.AppThem
import com.poulastaa.core.presentation.ui.PreviewCompactLandscape
import com.poulastaa.core.presentation.ui.PreviewLargeLandscape
import com.poulastaa.core.presentation.ui.PreviewSmallLandscape
import com.poulastaa.core.presentation.ui.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
internal fun OtpHorizontalCompactScreen(
    modifier: Modifier = Modifier,
    width: Float = 1f,
    shake: Animatable<Float, AnimationVector1D>,
    state: OtpUiState,
    onAction: (OtpUiAction) -> Unit,
) {
    val haptic = LocalHapticFeedback.current
    val focus = LocalFocusManager.current

    LaunchedEffect(state.otp.value.length) {
        if (state.otp.value.length == 5) focus.clearFocus()
    }


}

@PreviewLargeLandscape
@Composable
private fun Preview1() {
    AppThem(isSystemInDarkTheme()) {
        var state by remember { mutableStateOf(false) }
        val scope = rememberCoroutineScope()
        val shake = remember { Animatable(0f) }

        LaunchedEffect(Unit) {
            scope.launch {
                while (true) {
                    state = !state
                    delay(5_000)
                }
            }
        }

        LaunchedEffect(state) {
            if (state) {
                shake.animateTo(
                    targetValue = 0f,
                    animationSpec = keyframes {
                        durationMillis = 500

                        0f at 0
                        -12f at 40
                        12f at 80
                        -12f at 120
                        12f at 160
                        -10f at 200
                        10f at 240
                        -8f at 280
                        8f at 320
                        -5f at 360
                        5f at 400
                        -3f at 440
                        3f at 480
                        0f at 500
                    }
                )
            }
        }

        AppThem(isSystemInDarkTheme()) {
            OtpVerticalCompactScreen(
                width = .5f,
                shake = shake,
                state = OtpUiState(
                    resendState = ResendState.IDEAL,
                    email = "poulastaadas2@gmail.com",
                    otp = TextProp(
                        value = "1234",
                        isErr = state,
                        errText = UiText.StringResource(R.string.invalid_otp)
                    ),
                )
            ) { }
        }
    }
}

@PreviewCompactLandscape
@Composable
private fun Preview2() {
    AppThem(isSystemInDarkTheme()) {
        var state by remember { mutableStateOf(false) }
        val scope = rememberCoroutineScope()
        val shake = remember { Animatable(0f) }

        LaunchedEffect(Unit) {
            scope.launch {
                while (true) {
                    state = !state
                    delay(5_000)
                }
            }
        }

        LaunchedEffect(state) {
            if (state) {
                shake.animateTo(
                    targetValue = 0f,
                    animationSpec = keyframes {
                        durationMillis = 500

                        0f at 0
                        -12f at 40
                        12f at 80
                        -12f at 120
                        12f at 160
                        -10f at 200
                        10f at 240
                        -8f at 280
                        8f at 320
                        -5f at 360
                        5f at 400
                        -3f at 440
                        3f at 480
                        0f at 500
                    }
                )
            }
        }

        AppThem(isSystemInDarkTheme()) {
            OtpVerticalCompactScreen(
                width = .5f,
                shake = shake,
                state = OtpUiState(
                    resendState = ResendState.IDEAL,
                    email = "poulastaadas2@gmail.com",
                    otp = TextProp(
                        value = "1234",
                        isErr = state,
                        errText = UiText.StringResource(R.string.invalid_otp)
                    ),
                )
            ) { }
        }
    }
}

@PreviewSmallLandscape
@Composable
private fun Preview3() {
    AppThem(isSystemInDarkTheme()) {
        var state by remember { mutableStateOf(false) }
        val scope = rememberCoroutineScope()
        val shake = remember { Animatable(0f) }

        LaunchedEffect(Unit) {
            scope.launch {
                while (true) {
                    state = !state
                    delay(5_000)
                }
            }
        }

        LaunchedEffect(state) {
            if (state) {
                shake.animateTo(
                    targetValue = 0f,
                    animationSpec = keyframes {
                        durationMillis = 500

                        0f at 0
                        -12f at 40
                        12f at 80
                        -12f at 120
                        12f at 160
                        -10f at 200
                        10f at 240
                        -8f at 280
                        8f at 320
                        -5f at 360
                        5f at 400
                        -3f at 440
                        3f at 480
                        0f at 500
                    }
                )
            }
        }

        AppThem(isSystemInDarkTheme()) {
            OtpVerticalCompactScreen(
                width = .5f,
                shake = shake,
                state = OtpUiState(
                    resendState = ResendState.IDEAL,
                    email = "poulastaadas2@gmail.com",
                    otp = TextProp(
                        value = "1234",
                        isErr = state,
                        errText = UiText.StringResource(R.string.invalid_otp)
                    ),
                )
            ) { }
        }
    }
}