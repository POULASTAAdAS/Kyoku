package com.poulastaa.auth.presentation.email.signup

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.poulastaa.auth.presentation.email.signup.components.EmailSignUpCompact
import com.poulastaa.auth.presentation.email.signup.components.EmailSignUpExpanded
import com.poulastaa.auth.presentation.email.signup.components.EmailSignUpMedium
import com.poulastaa.core.domain.ScreenEnum
import com.poulastaa.core.presentation.designsystem.components.ScreenWrapper
import com.poulastaa.core.presentation.ui.ObserveAsEvent

@Composable
fun EmailSignUpRootScreen(
    viewModel: EmailSignUpViewModel = hiltViewModel(),
    navigateBack: () -> Unit,
    navigate: (screen: ScreenEnum) -> Unit,
) {
    val context = LocalContext.current

    ObserveAsEvent(flow = viewModel.uiEvent) { event ->
        when (event) {
            is EmailSignUpUiAction.EmitToast -> {
                Toast.makeText(
                    context,
                    event.message.asString(context),
                    Toast.LENGTH_LONG
                ).show()
            }

            is EmailSignUpUiAction.OnSuccess -> navigate(event.screen)

            EmailSignUpUiAction.OnEmailLogIn -> navigateBack()
        }
    }

    EmailSignUpScreen(
        state = viewModel.state,
        onEvent = viewModel::onEvent
    )


    BackHandler {
        navigateBack()
    }
}

@Composable
fun EmailSignUpScreen(
    state: EmailSignUpUiState,
    onEvent: (EmailSignUpUiEvent) -> Unit,
) {
    ScreenWrapper(
        compactContent = {
            EmailSignUpCompact(
                state = state,
                onEvent = onEvent
            )
        },
        mediumContent = {
            EmailSignUpMedium(
                state = state,
                onEvent = onEvent
            )
        },
        expandedContent = {
            EmailSignUpExpanded(
                state = state,
                onEvent = onEvent
            )
        }
    )
}
