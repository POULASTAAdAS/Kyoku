package com.poulastaa.auth.presentation.email.login

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.poulastaa.auth.presentation.email.login.components.EmailLoginCompactScreen
import com.poulastaa.core.domain.ScreenEnum
import com.poulastaa.core.presentation.designsystem.components.ScreenWrapper
import com.poulastaa.core.presentation.ui.ObserveAsEvent

@Composable
fun EmailLoginRootScreen(
    viewModel: EmailLoginViewModel = hiltViewModel(),
    navigateToEmailSignUp: () -> Unit,
    navigateToForgotPassword: (email: String?) -> Unit,
    navigate: (ScreenEnum) -> Unit,
) {
    val context = LocalContext.current

    ObserveAsEvent(flow = viewModel.uiAction) { event ->
        when (event) {
            is EmailLoginUiAction.EmitToast -> {
                Toast.makeText(
                    context,
                    event.message.asString(context),
                    Toast.LENGTH_LONG
                ).show()
            }

            is EmailLoginUiAction.OnSuccess -> navigate(event.route)

            is EmailLoginUiAction.OnForgotPassword -> navigateToForgotPassword(event.email)

            EmailLoginUiAction.OnEmailSignUp -> navigateToEmailSignUp()
        }
    }

    EmailLoginScreen(
        state = viewModel.state,
        onEvent = viewModel::onEvent
    )
}

@Composable
private fun EmailLoginScreen(
    state: EmailLogInUiState,
    onEvent: (EmailLoginUiEvent) -> Unit,
) {
    ScreenWrapper(
        compactContent = {
            EmailLoginCompactScreen(
                state = state,
                onEvent = onEvent
            )
        },
        mediumContent = {

        },
        expandedContent = {

        }
    )
}