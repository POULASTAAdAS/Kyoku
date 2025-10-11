package com.poulastaa.auth.presentation.otp

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.keyframes
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.poulastaa.auth.presentation.intro.components.ConformButton
import com.poulastaa.auth.presentation.otp.components.Info
import com.poulastaa.core.presentation.designsystem.TextProp
import com.poulastaa.core.presentation.designsystem.UiText
import com.poulastaa.core.presentation.ui.AppTheme
import com.poulastaa.core.presentation.ui.PreviewCompactLandscape
import com.poulastaa.core.presentation.ui.PreviewLargeLandscape
import com.poulastaa.core.presentation.ui.PreviewSmallLandscape
import com.poulastaa.core.presentation.ui.R
import com.poulastaa.core.presentation.ui.dimens
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
internal fun OtpHorizontalCompactScreen(
    modifier: Modifier = Modifier,
    isExtendedExtended: Boolean = false,
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

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            VerificationTopBar(haptic, onAction)
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(
                    start = MaterialTheme.dimens.medium1,
                    end = MaterialTheme.dimens.medium1,
                    bottom = MaterialTheme.dimens.medium1
                )
                .then(modifier),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Info(
                heading = state.email,
                content = stringResource(R.string.verify_content)
            )

            Spacer(
                Modifier.height(
                    if (isExtendedExtended) MaterialTheme.dimens.large2
                    else MaterialTheme.dimens.small1
                )
            )

            OTPValidationTextField(width, state.otp, onAction, shake)
            ReSendOrNavigateBack(
                state,
                haptic,
                onAction,
                height = (if (isExtendedExtended) 80.dp else 60.dp)
            )
            AnimatedVisibility(state.isTryAnotherEmailVisible) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Info(
                        heading = stringResource(R.string.try_other_mail),
                        content = stringResource(R.string.didnt_receive_otp),
                        isClickable = true,
                    ) {
                        onAction(OtpUiAction.OnDirectBack)
                    }
                }
            }

            Spacer(
                Modifier.height(
                    if (isExtendedExtended) MaterialTheme.dimens.large2
                    else MaterialTheme.dimens.medium1
                )
            )

            ConformButton(
                modifier = Modifier.fillMaxWidth(.25f),
                isLoading = state.isLoading,
                heading = stringResource(R.string.verify_text),
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    onAction(OtpUiAction.OnSubmit)
                }
            )
        }
    }
}

@PreviewLargeLandscape
@Composable
private fun Preview1() {
    AppTheme(isSystemInDarkTheme()) {
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

        AppTheme(isSystemInDarkTheme()) {
            OtpHorizontalCompactScreen(
                isExtendedExtended = true,
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
    AppTheme(isSystemInDarkTheme()) {
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

        AppTheme(isSystemInDarkTheme()) {
            OtpHorizontalCompactScreen(
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
    AppTheme(isSystemInDarkTheme()) {
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

        AppTheme(isSystemInDarkTheme()) {
            OtpHorizontalCompactScreen(
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