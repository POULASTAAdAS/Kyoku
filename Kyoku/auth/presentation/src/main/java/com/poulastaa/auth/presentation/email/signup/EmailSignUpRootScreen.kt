package com.poulastaa.auth.presentation.email.signup

import android.app.Activity
import android.widget.Toast
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.poulastaa.core.presentation.ui.KyokuWindowSize
import com.poulastaa.core.presentation.ui.ObserveAsEvent

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun EmailSignUpRootScreen(
    viewModel: EmailSignUpViewModel = hiltViewModel(),
    navigateToLogin: () -> Unit,
) {
    val context = LocalContext.current as Activity
    val windowSize = calculateWindowSizeClass(context)
    val config = LocalConfiguration.current

    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvent(viewModel.uiEvent) { event ->
        when (event) {
            is EmailSignUpUiEvent.EmitToast -> Toast.makeText(
                context,
                event.message.asString(context),
                Toast.LENGTH_LONG
            ).show()

            EmailSignUpUiEvent.NavigateToLogIn -> navigateToLogin()

            EmailSignUpUiEvent.OnSuccess -> {
                // todo
            }
        }
    }

    KyokuWindowSize(
        windowSizeClass = windowSize,
        compactContent = {
            EmailSignUpCompactScreen(
                state = state,
                onAction = viewModel::onAction
            )
        },
        mediumContent = {

        },
        expandedContent = {

        }
    )
}