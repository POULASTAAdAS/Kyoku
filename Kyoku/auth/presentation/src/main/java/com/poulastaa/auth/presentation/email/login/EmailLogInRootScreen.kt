package com.poulastaa.auth.presentation.email.login

import android.app.Activity
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.poulastaa.core.presentation.ui.KyokuWindowSize

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun EmailLogInRootScreen(
    viewModel: EmailViewModel = hiltViewModel(),
    navigateToEmailSignUp: () -> Unit,
    navigateToForgotPassword: (String?) -> Unit,
) {
    val context = LocalContext.current
    val windowSize = calculateWindowSizeClass(context as Activity)
    val state by viewModel.state.collectAsStateWithLifecycle()

    KyokuWindowSize(
        windowSizeClass = windowSize,
        compactContent = {
            EmailLogInCompactScreen(
                state = state,
                onAction = viewModel::onAction
            )
        },
        mediumContent = {
            EmailLogInCompactScreen(
                state = state,
                onAction = viewModel::onAction
            )
        },
        expandedContent = {

        }
    )
}