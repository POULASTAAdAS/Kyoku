package com.poulastaa.auth.presentation.reset_password

import android.widget.Toast
import androidx.activity.compose.LocalActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.poulastaa.core.domain.JWTToken
import com.poulastaa.core.presentation.KyokuWindowSize
import com.poulastaa.core.presentation.circularConsumeAnimation
import com.poulastaa.core.presentation.designsystem.ObserveAsEvent
import com.poulastaa.core.presentation.ui.CheckIcon
import com.poulastaa.core.presentation.ui.R
import com.poulastaa.core.presentation.ui.dimens

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
internal fun ResetPasswordRootScreen(
    token: JWTToken,
    viewmodel: ResetPasswordViewmodel = hiltViewModel(),
    popUpToLogIn: () -> Unit,
    navigateBack: () -> Unit,
) {
    var triggerAnimationOffSet by remember { mutableStateOf(Offset(0f, 0f)) }
    var center by remember { mutableStateOf(Offset(0f, 0f)) }
    var isAnimationDone by remember { mutableStateOf(false) }

    LaunchedEffect(token) {
        viewmodel.loadToken(token)
    }

    val activity = LocalActivity.current ?: return
    val state by viewmodel.state.collectAsStateWithLifecycle()
    val haptic = LocalHapticFeedback.current

    ObserveAsEvent(viewmodel.uiEvent) { event ->
        when (event) {
            is ResetPasswordUiEvent.EmitToast -> Toast.makeText(
                activity,
                event.message.asString(activity),
                Toast.LENGTH_LONG
            ).show()

            ResetPasswordUiEvent.TriggerAnimation -> triggerAnimationOffSet = center

            ResetPasswordUiEvent.PopUpToIntroScreen -> {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                popUpToLogIn()
            }

            ResetPasswordUiEvent.NavigateBack -> navigateBack()
        }
    }

    val density = LocalDensity.current
    val configuration = LocalConfiguration.current
    val triple = circularConsumeAnimation(
        density,
        config = configuration,
        offset = triggerAnimationOffSet,
        animationTimeMileSec = 1000,
        resetAnimation = {
            isAnimationDone = true
        }
    )

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        KyokuWindowSize(
            windowSizeClass = calculateWindowSizeClass(activity),
            compactContent = {
                ResetPasswordVerticalScreen(
                    state = state,
                    haptic = haptic,
                    onAction = viewmodel::onAction
                )
            },
            mediumContent = {
                ResetPasswordVerticalScreen(
                    modifier = Modifier.padding(MaterialTheme.dimens.medium1),
                    state = state,
                    haptic = haptic,
                    onAction = viewmodel::onAction
                )
            },
            expandedSmallContent = {
                ResetPasswordHorizontalCompactScreen(
                    state = state,
                    haptic = haptic,
                    onAction = viewmodel::onAction
                )
            },
            expandedCompactContent = {
                ResetPasswordHorizontalCompactScreen(
                    state = state,
                    haptic = haptic,
                    onAction = viewmodel::onAction
                )
            },
            expandedLargeContent = {
                ResetPasswordHorizontalCompactScreen(
                    modifier = Modifier.padding(MaterialTheme.dimens.medium1),
                    state = state,
                    haptic = haptic,
                    onAction = viewmodel::onAction
                )
            }
        )

        Canvas(modifier = Modifier.fillMaxSize()) {
            center = Offset(size.width / 2f, size.height / 2f)

            if (triple.revealSize.value > 0) {
                drawCircle(
                    color = triple.backgroundColor,
                    radius = triple.revealSize.value,
                    center = triple.center
                )
            }
        }

        AnimatedVisibility(
            isAnimationDone,
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center),
            enter = fadeIn(animationSpec = tween(durationMillis = 800))
                    + expandIn(animationSpec = tween(durationMillis = 800))
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = CheckIcon,
                    contentDescription = stringResource(R.string.reset),
                    modifier = Modifier
                        .fillMaxSize(.3f)
                        .aspectRatio(1f)
                )

                Text(text = stringResource(R.string.login_again_to_continue))
            }
        }
    }
}