package com.poulastaa.auth.presentation.email.login

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.autofill.AutofillNode
import androidx.compose.ui.autofill.AutofillType
import androidx.compose.ui.platform.LocalAutofill
import androidx.compose.ui.platform.LocalAutofillTree
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.poulastaa.auth.presentation.email.login.components.EmailLoginCompactScreen
import com.poulastaa.auth.presentation.email.login.components.EmailLoginExpandedScreen
import com.poulastaa.auth.presentation.email.login.components.EmailLoginMediumScreen
import com.poulastaa.core.domain.ScreenEnum
import com.poulastaa.core.presentation.designsystem.components.AuthScreenWrapper
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

    BackHandler {
        navigate(ScreenEnum.INTRO)
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun EmailLoginScreen(
    state: EmailLogInUiState,
    onEvent: (EmailLoginUiEvent) -> Unit,
) {
    val autoFillEmail = AutofillNode(
        autofillTypes = listOf(AutofillType.EmailAddress),
        onFill = {
            onEvent(EmailLoginUiEvent.OnEmailChange(it))
        }
    )

    val autoFillPassword = AutofillNode(
        autofillTypes = listOf(AutofillType.Password),
        onFill = {
            onEvent(EmailLoginUiEvent.OnPasswordChange(it))
        }
    )


    val autoFill = LocalAutofill.current
    LocalAutofillTree.current += autoFillEmail
    LocalAutofillTree.current += autoFillPassword


    AuthScreenWrapper(
        compactContent = {
            EmailLoginCompactScreen(
                autoFillEmail = autoFillEmail,
                autoFillPassword = autoFillPassword,
                autoFill = autoFill,
                state = state,
                onEvent = onEvent
            )
        },
        mediumContent = {
            EmailLoginMediumScreen(
                autoFillEmail = autoFillEmail,
                autoFillPassword = autoFillPassword,
                autoFill = autoFill,
                state = state,
                onEvent = onEvent
            )
        },
        expandedContent = {
            EmailLoginExpandedScreen(
                autoFillEmail = autoFillEmail,
                autoFillPassword = autoFillPassword,
                autoFill = autoFill,
                state = state,
                onEvent = onEvent
            )
        }
    )
}