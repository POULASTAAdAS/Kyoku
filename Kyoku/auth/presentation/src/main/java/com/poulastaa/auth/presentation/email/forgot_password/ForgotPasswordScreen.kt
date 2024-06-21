package com.poulastaa.auth.presentation.email.forgot_password

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.autofill.AutofillNode
import androidx.compose.ui.autofill.AutofillType
import androidx.compose.ui.platform.LocalAutofill
import androidx.compose.ui.platform.LocalAutofillTree
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.poulastaa.auth.presentation.email.forgot_password.components.ForgotPasswordCompact
import com.poulastaa.auth.presentation.email.forgot_password.components.ForgotPasswordMedium
import com.poulastaa.core.presentation.designsystem.components.ScreenWrapper
import com.poulastaa.core.presentation.ui.ObserveAsEvent

@Composable
fun ForgotPasswordRootScreen(
    viewModel: ForgotPasswordViewModel = hiltViewModel(),
    navigateBack: () -> Unit,
) {
    val context = LocalContext.current

    ObserveAsEvent(flow = viewModel.uiAction) { event ->
        when (event) {
            is ForgotPasswordUiAction.EmitToast -> {
                Toast.makeText(
                    context,
                    event.message.asString(context),
                    Toast.LENGTH_LONG
                ).show()
            }

            ForgotPasswordUiAction.NavigateBack -> navigateBack()
        }
    }

    ForgotPasswordScreen(
        state = viewModel.state,
        onEvent = viewModel::onEvent
    )

    BackHandler {
        viewModel.onEvent(ForgotPasswordUiEvent.OnBackClick)
    }
}


@Composable
private fun ForgotPasswordScreen(
    state: ForgotPasswordUiState,
    onEvent: (ForgotPasswordUiEvent) -> Unit,
) {
    ScreenWrapper(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        compactContent = {
            ForgotPasswordCompact(
                state = state,
                onEvent = onEvent
            )
        },
        mediumContent = {
            ForgotPasswordMedium(
                state = state,
                onEvent = onEvent
            )
        },
        expandedContent = {
            ForgotPasswordMedium(
                state = state,
                onEvent = onEvent
            )
        }
    )
}
