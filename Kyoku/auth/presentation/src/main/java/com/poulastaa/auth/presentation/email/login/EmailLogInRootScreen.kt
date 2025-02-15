package com.poulastaa.auth.presentation.email.login

import android.app.Activity
import android.widget.Toast
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.autofill.AutofillNode
import androidx.compose.ui.autofill.AutofillType
import androidx.compose.ui.platform.LocalAutofill
import androidx.compose.ui.platform.LocalAutofillTree
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.poulastaa.core.domain.model.SavedScreen
import com.poulastaa.core.presentation.designsystem.KyokuWindowSize
import com.poulastaa.core.presentation.designsystem.ObserveAsEvent

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class, ExperimentalComposeUiApi::class)
@Composable
fun EmailLogInRootScreen(
    viewModel: EmailLogInViewModel = hiltViewModel(),
    navigateToEmailSignUp: () -> Unit,
    navigateToForgotPassword: (String?) -> Unit,
    navigateToScreen: (SavedScreen) -> Unit,
) {
    val context = LocalContext.current
    val windowSize = calculateWindowSizeClass(context as Activity)
    val state by viewModel.state.collectAsStateWithLifecycle()
    val config = LocalConfiguration.current

    val autoFill = LocalAutofill.current
    val autoFillEmail = AutofillNode(
        autofillTypes = listOf(AutofillType.EmailAddress),
        onFill = {
            viewModel.onAction(EmailLogInUiAction.OnEmailChange(it))
        }
    )
    val autoFillPassword = AutofillNode(
        autofillTypes = listOf(AutofillType.Password),
        onFill = {
            viewModel.onAction(EmailLogInUiAction.OnPasswordChange(it))
        }
    )

    val tree = LocalAutofillTree.current
    tree += autoFillEmail
    tree += autoFillPassword

    ObserveAsEvent(viewModel.uiEvent) { event ->
        when (event) {
            is EmailLogInUiEvent.EmitToast -> Toast.makeText(
                context,
                event.message.asString(context),
                Toast.LENGTH_LONG
            ).show()

            is EmailLogInUiEvent.NavigateToForgotPassword -> navigateToForgotPassword(event.email)

            EmailLogInUiEvent.NavigateToSignUp -> navigateToEmailSignUp()

            is EmailLogInUiEvent.OnSuccess -> navigateToScreen(event.screen)
        }
    }

    KyokuWindowSize(
        windowSizeClass = windowSize,
        compactContent = {
            EmailLogInCompactScreen(
                autoFill = autoFill,
                autoFillEmail = autoFillEmail,
                autoFillPassword = autoFillPassword,
                state = state,
                onAction = viewModel::onAction
            )
        },
        mediumContent = {
            EmailLogInMediumScreen(
                autoFill = autoFill,
                autoFillEmail = autoFillEmail,
                autoFillPassword = autoFillPassword,
                state = state,
                onAction = viewModel::onAction
            )
        },
        expandedContent = {
            if (config.screenWidthDp > 980)
                EmailLogInExpandedScreen(
                    autoFill = autoFill,
                    autoFillEmail = autoFillEmail,
                    autoFillPassword = autoFillPassword,
                    state = state,
                    onAction = viewModel::onAction
                )
            else EmailLogInCompactExpandedScreen(
                autoFill = autoFill,
                autoFillEmail = autoFillEmail,
                autoFillPassword = autoFillPassword,
                state = state,
                onAction = viewModel::onAction
            )
        }
    )
}