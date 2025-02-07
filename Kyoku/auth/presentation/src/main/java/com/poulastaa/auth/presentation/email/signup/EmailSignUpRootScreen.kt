package com.poulastaa.auth.presentation.email.signup

import android.app.Activity
import android.widget.Toast
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.autofill.AutofillNode
import androidx.compose.ui.autofill.AutofillType
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.platform.LocalAutofill
import androidx.compose.ui.platform.LocalAutofillTree
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.poulastaa.core.domain.model.SavedScreen
import com.poulastaa.core.presentation.ui.KyokuWindowSize
import com.poulastaa.core.presentation.ui.ObserveAsEvent

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class, ExperimentalComposeUiApi::class)
@Composable
fun EmailSignUpRootScreen(
    viewModel: EmailSignUpViewModel = hiltViewModel(),
    navigateToLogin: () -> Unit,
    navigateToScreen: (SavedScreen) -> Unit,
) {
    val context = LocalContext.current as Activity
    val windowSize = calculateWindowSizeClass(context)
    val config = LocalConfiguration.current

    val state by viewModel.state.collectAsStateWithLifecycle()
    val autoFill = LocalAutofill.current
    val autoFillPassword = AutofillNode(
        autofillTypes = listOf(AutofillType.Password),
        onFill = {
            viewModel.onAction(EmailSignUpUiAction.OnPasswordChange(it))
            viewModel.onAction(EmailSignUpUiAction.OnConformPasswordChange(it))
        }
    )
    val autoFillUserName = AutofillNode(
        autofillTypes = listOf(AutofillType.NewUsername),
        onFill = {
            viewModel.onAction(EmailSignUpUiAction.OnUsernameChange(it))
        }
    )

    LocalAutofillTree.current += autoFillPassword
    LocalAutofillTree.current += autoFillUserName

    ObserveAsEvent(viewModel.uiEvent) { event ->
        when (event) {
            is EmailSignUpUiEvent.EmitToast -> Toast.makeText(
                context,
                event.message.asString(context),
                Toast.LENGTH_LONG
            ).show()

            EmailSignUpUiEvent.NavigateToLogIn -> navigateToLogin()

            is EmailSignUpUiEvent.OnSuccess -> navigateToScreen(event.screen)
        }
    }

    KyokuWindowSize(
        windowSizeClass = windowSize,
        compactContent = {
            EmailSignUpCompactScreen(
                autoFill = autoFill,
                autoFillPassword = autoFillPassword,
                autoFillUserName = autoFillUserName,
                state = state,
                onAction = viewModel::onAction
            )
        },
        mediumContent = {
            EmailSignUpMediumScreen(
                autoFill = autoFill,
                autoFillPassword = autoFillPassword,
                autoFillUserName = autoFillUserName,
                state = state,
                onAction = viewModel::onAction
            )
        },
        expandedContent = {
            if (config.screenWidthDp > 980) EmailSignUpExpandedScreen(
                autoFill = autoFill,
                autoFillPassword = autoFillPassword,
                autoFillUserName = autoFillUserName,
                state = state,
                onAction = viewModel::onAction
            ) else EmailSignUpCompactExpandedScreen(
                autoFill = autoFill,
                autoFillPassword = autoFillPassword,
                autoFillUserName = autoFillUserName,
                state = state,
                onAction = viewModel::onAction
            )
        }
    )
}