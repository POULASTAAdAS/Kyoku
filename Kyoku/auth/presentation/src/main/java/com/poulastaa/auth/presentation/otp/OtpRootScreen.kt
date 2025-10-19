package com.poulastaa.auth.presentation.otp

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.keyframes
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.poulastaa.auth.presentation.otp.components.FinalAlertDialog
import com.poulastaa.auth.presentation.otp.components.FirstAlertDialog
import com.poulastaa.core.domain.Email
import com.poulastaa.core.domain.JWTToken
import com.poulastaa.core.presentation.KyokuWindowSize
import com.poulastaa.core.presentation.designsystem.ObserveAsEvent
import com.poulastaa.core.presentation.ui.dimens
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
internal fun OtpRootScreen(
    email: Email,
    viewmodel: OtpViewmodel = hiltViewModel(),
    navigateToUpdatePassword: (token: JWTToken) -> Unit,
    navigateBack: () -> Unit,
) {
    LaunchedEffect(email) {
        viewmodel.loadEmail(email)
    }

    val activity = LocalActivity.current ?: return
    val shake: Animatable<Float, AnimationVector1D> = remember { Animatable(0f) }
    val state by viewmodel.uiState.collectAsStateWithLifecycle()
    val haptic = LocalHapticFeedback.current
    val scope = rememberCoroutineScope()

    ObserveAsEvent(viewmodel.uiEvent) { event ->
        when (event) {
            is OtpUiEvent.EmitToast -> Toast.makeText(
                activity,
                event.message.asString(activity),
                Toast.LENGTH_LONG
            ).show()

            OtpUiEvent.NavigateBack -> navigateBack()

            is OtpUiEvent.NavigateToResetPassword -> navigateToUpdatePassword(event.token)
        }
    }


    LaunchedEffect(state.otp.isErr) {
        if (state.otp.isErr) scope.launch {
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
            ).also { haptic.performHapticFeedback(HapticFeedbackType.LongPress) }
        }
    }
    LaunchedEffect(state.otp.value.length) {
        if (state.otp.value.length == 5) viewmodel.onAction(OtpUiAction.OnSubmit)
    }

    when (state.returnState) {
        ReturnState.IDEAL -> Unit
        ReturnState.WARNING -> FirstAlertDialog(
            onCancel = {
                viewmodel.onAction(OtpUiAction.OnBackCancel)
            },
            onConform = {
                viewmodel.onAction(OtpUiAction.OnBackConform)
            }
        )

        ReturnState.CONFIRM -> FinalAlertDialog(
            onCancel = {
                viewmodel.onAction(OtpUiAction.OnBackCancel)
            },
            onConform = {
                viewmodel.onAction(OtpUiAction.OnBackConform)
            }
        )
    }

    KyokuWindowSize(
        windowSizeClass = calculateWindowSizeClass(activity),
        compactContent = {
            OtpVerticalCompactScreen(
                shake = shake,
                state = state,
                onAction = viewmodel::onAction
            )
        },
        mediumContent = {
            OtpVerticalCompactScreen(
                modifier = Modifier.padding(MaterialTheme.dimens.small3),
                width = .6f,
                shake = shake,
                state = state,
                onAction = viewmodel::onAction
            )
        },
        expandedSmallContent = {
            OtpHorizontalCompactScreen(
                width = .4f,
                shake = shake,
                state = state,
                onAction = viewmodel::onAction
            )
        },
        expandedCompactContent = {
            OtpHorizontalCompactScreen(
                width = .4f,
                shake = shake,
                state = state,
                onAction = viewmodel::onAction
            )
        },
        expandedLargeContent = {
            OtpHorizontalCompactScreen(
                modifier = Modifier.padding(MaterialTheme.dimens.small3),
                isExtendedExtended = true,
                width = .5f,
                shake = shake,
                state = state,
                onAction = viewmodel::onAction
            )
        }
    )

    BackHandler {
        viewmodel.onAction(OtpUiAction.OnStateNavigateBackFlow)
    }
}