package com.poulastaa.auth.presentation.intro

import android.app.Activity
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.poulastaa.auth.presentation.intro.components.IntroCompactScreen
import com.poulastaa.auth.presentation.intro.components.IntroExpandedScreen
import com.poulastaa.auth.presentation.intro.components.IntroMediumScreen
import com.poulastaa.auth.presentation.intro.components.StartActivityForResult
import com.poulastaa.core.domain.ScreenEnum
import com.poulastaa.core.presentation.designsystem.components.AuthScreenWrapper
import com.poulastaa.core.presentation.ui.ObserveAsEvent

@Composable
fun IntroRootScreen(
    viewModel: IntroViewModel = hiltViewModel(),
    navigate: (ScreenEnum) -> Unit,
) {
    val context = LocalContext.current

    ObserveAsEvent(flow = viewModel.uiEvent) { event ->
        when (event) {
            is IntroUiAction.EmitToast -> {
                Toast.makeText(
                    context,
                    event.message.asString(context),
                    Toast.LENGTH_LONG
                ).show()
            }

            is IntroUiAction.OnSuccess -> navigate(event.screen)
        }
    }

    IntroScreen(
        state = viewModel.state,
        onEvent = viewModel::onEvent
    )
}

@Composable
private fun IntroScreen(
    state: IntroUiState,
    onEvent: (IntroUiEvent) -> Unit,
) {
    val context = LocalContext.current

    StartActivityForResult(
        key = state.isGoogleLogging,
        activity = context as Activity,
        clientId = state.clientId,
        onSuccess = {
            onEvent(
                IntroUiEvent.OnTokenReceive(
                    token = it,
                    activity = context
                )
            )
        },
        onCanceled = {
            onEvent(IntroUiEvent.OnGoogleLogInCancel)
        }
    )

    AuthScreenWrapper(
        compactContent = {
            IntroCompactScreen(
                state = state,
                onEvent = onEvent
            )
        },
        mediumContent = {
            IntroMediumScreen(
                state = state,
                onEvent = onEvent
            )
        },
        expandedContent = {
            IntroExpandedScreen(
                state = state,
                onEvent = onEvent
            )
        }
    )
}