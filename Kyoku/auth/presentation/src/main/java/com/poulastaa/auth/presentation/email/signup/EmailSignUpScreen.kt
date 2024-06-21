package com.poulastaa.auth.presentation.email.signup

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

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun EmailSignUpScreen(
    state: EmailSignUpUiState,
    onEvent: (EmailSignUpUiEvent) -> Unit,
) {
    val autoFillEmail = AutofillNode(
        autofillTypes = listOf(AutofillType.EmailAddress),
        onFill = {
            onEvent(EmailSignUpUiEvent.OnEmailChange(it))
        }
    )

    val autoFillPassword = AutofillNode(
        autofillTypes = listOf(AutofillType.Password),
        onFill = {
            onEvent(EmailSignUpUiEvent.OnPasswordChange(it))
            onEvent(EmailSignUpUiEvent.OnConfirmPasswordChange(it))
        }
    )

    val autoFillUserName = AutofillNode(
        autofillTypes = listOf(AutofillType.NewUsername),
        onFill = {
            onEvent(EmailSignUpUiEvent.OnUserNameChange(it))
        }
    )

    val autoFill = LocalAutofill.current
    LocalAutofillTree.current += autoFillEmail
    LocalAutofillTree.current += autoFillPassword
    LocalAutofillTree.current += autoFillUserName

    ScreenWrapper(
        compactContent = {
            EmailSignUpCompact(
                autoFillUserName = autoFillUserName,
                autoFillEmail = autoFillEmail,
                autoFillPassword = autoFillPassword,
                autoFill = autoFill,
                state = state,
                onEvent = onEvent
            )
        },
        mediumContent = {
            EmailSignUpMedium(
                autoFillUserName = autoFillUserName,
                autoFillEmail = autoFillEmail,
                autoFillPassword = autoFillPassword,
                autoFill = autoFill,
                state = state,
                onEvent = onEvent
            )
        },
        expandedContent = {
            EmailSignUpExpanded(
                autoFillUserName = autoFillUserName,
                autoFillEmail = autoFillEmail,
                autoFillPassword = autoFillPassword,
                autoFill = autoFill,
                state = state,
                onEvent = onEvent
            )
        }
    )
}
