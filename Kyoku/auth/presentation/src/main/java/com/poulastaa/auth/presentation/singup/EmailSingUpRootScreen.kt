package com.poulastaa.auth.presentation.singup

import android.widget.Toast
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.poulastaa.auth.presentation.components.AuthAllowedNavigationScreen
import com.poulastaa.core.domain.utils.Email
import com.poulastaa.core.presentation.KyokuWindowSize
import com.poulastaa.core.presentation.designsystem.ObserveAsEvent
import com.poulastaa.core.presentation.ui.dimens

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
internal fun EmailSingUpRootScreen(
    email: Email?,
    viewmodel: EmailSingUpViewmodel = hiltViewModel(),
    navigateToSetUp: () -> Unit,
    navigateBack: () -> Unit,
) {
    val activity = LocalActivity.current ?: return

    LaunchedEffect(email) {
        email?.let { viewmodel.populateEmail(email) }
    }

    ObserveAsEvent(viewmodel.uiEvent) { event ->
        when (event) {
            is EmailSingUpUiEvent.EmitToast -> Toast.makeText(
                activity,
                event.message.asString(activity),
                Toast.LENGTH_LONG
            ).show()

            EmailSingUpUiEvent.OnNavigateBack -> navigateBack()
            is EmailSingUpUiEvent.NavigateToSetUp -> navigateToSetUp()
        }
    }

    val state by viewmodel.state.collectAsStateWithLifecycle()

    KyokuWindowSize(
        windowSizeClass = calculateWindowSizeClass(activity),
        compactContent = {
            EmailSingUpVerticalScreen(
                state = state,
                onAction = viewmodel::onAction
            )
        },
        mediumContent = {
            EmailSingUpVerticalScreen(
                modifier = Modifier.padding(MaterialTheme.dimens.small3),
                state = state,
                onAction = viewmodel::onAction
            )
        },
        expandedSmallContent = {
            EmailSingUpVerticalCompactScreen(
                state = state,
                onAction = viewmodel::onAction
            )
        },
        expandedCompactContent = {
            EmailSingUpVerticalCompactScreen(
                state = state,
                onAction = viewmodel::onAction
            )
        },
        expandedLargeContent = {
            EmailSingUpVerticalExpandedScreen(
                state = state,
                onAction = viewmodel::onAction
            )
        }
    )
}