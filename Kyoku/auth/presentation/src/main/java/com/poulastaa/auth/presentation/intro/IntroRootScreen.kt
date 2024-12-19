package com.poulastaa.auth.presentation.intro

import android.app.Activity
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
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

    StartActivityForResult(
        key = viewmodel.state.isGoogleAuthLoading,
        activity = activity,
        clientId = viewmodel.state.clientId,
        onSuccess = {
            viewmodel.onAction(
                IntroUiAction.OnTokenReceive(
                    token = it,
                    activity = activity
                )
            )
        },
        onCanceled = {
            viewmodel.onAction(IntroUiAction.OnGoogleSignInCancel)
        }
    )

    KyokuWindowSize(
        windowSizeClass = windowSize,
        compactContent = {
            IntroCompactScreen(
                state = viewmodel.state,
                navigateToEmailLogIn = navigateToEmailLogIn,
                onAction = viewmodel::onAction
            )
        },
        mediumContent = {
            IntroMediumScreen(
                state = viewmodel.state,
                navigateToEmailLogIn = navigateToEmailLogIn,
                onAction = viewmodel::onAction
            )
        },
        expandedContent = {
            IntroExpandedScreen(
                state = viewmodel.state,
                navigateToEmailLogIn = navigateToEmailLogIn,
                onAction = viewmodel::onAction
            )
        }
    )
}
