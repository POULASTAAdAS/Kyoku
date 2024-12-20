package com.poulastaa.auth.presentation.email.forgot_password

import android.app.Activity
import android.widget.Toast
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.poulastaa.core.presentation.ui.KyokuWindowSize
import com.poulastaa.core.presentation.ui.ObserveAsEvent

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun ForgotPasswordRootScreen(
    viewModel: ForgotPasswordViewModel,
    navigateBack: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val context = LocalContext.current as Activity
    val windowSizeClass = calculateWindowSizeClass(context)

    ObserveAsEvent(viewModel.uiEvent) { event ->
        when (event) {
            is ForgotPasswordUiEvent.EmitToast -> Toast.makeText(
                context,
                event.message.asString(context),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    KyokuWindowSize(
        windowSizeClass = windowSizeClass,
        compactContent = {
            ForgotPasswordCompactScreen(
                state = state,
                onAction = viewModel::onAction,
                navigateBack = navigateBack
            )
        },
        mediumContent = {
            ForgotPasswordMediumScreen(
                state = state,
                onAction = viewModel::onAction,
                navigateBack = navigateBack
            )
        },
        expandedContent = {
            ForgotPasswordMediumScreen(
                state = state,
                onAction = viewModel::onAction,
                navigateBack = navigateBack
            )
        }
    )
}