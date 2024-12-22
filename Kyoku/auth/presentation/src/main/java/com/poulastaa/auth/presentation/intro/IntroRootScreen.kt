package com.poulastaa.auth.presentation.intro

import android.app.Activity
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.poulastaa.auth.presentation.intro.components.StartActivityForResult
import com.poulastaa.core.presentation.ui.KyokuWindowSize

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun IntroRootScreen(
    viewmodel: IntroViewmodel = hiltViewModel(),
    navigateToEmailLogIn: () -> Unit,
) {
    val activity = LocalContext.current as Activity
    val windowSize = calculateWindowSizeClass(activity)
    val state by viewmodel.state.collectAsStateWithLifecycle()

    KyokuWindowSize(
        windowSizeClass = windowSize,
        compactContent = {
            IntroCompactScreen(
                state = state,
                navigateToEmailLogIn = navigateToEmailLogIn,
                onAction = viewmodel::onAction
            )
        },
        mediumContent = {
            IntroMediumScreen(
                state = state,
                navigateToEmailLogIn = navigateToEmailLogIn,
                onAction = viewmodel::onAction
            )
        },
        expandedContent = {
            IntroExpandedScreen(
                state = state,
                navigateToEmailLogIn = navigateToEmailLogIn,
                onAction = viewmodel::onAction
            )
        }
    )

    StartActivityForResult(
        key = state.isGoogleAuthLoading,
        activity = activity,
        clientId = state.clientId,
        onSuccess = {
            viewmodel.onAction(
                IntroUiAction.OnTokenReceive(
                    token = it,
                    countryCode = "//todo" // todo
                )
            )
        },
        onCanceled = {
            viewmodel.onAction(IntroUiAction.OnGoogleSignInCancel)
        }
    )
}
