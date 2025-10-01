package com.poulastaa.auth.presentation.singup

import android.widget.Toast
import androidx.activity.compose.LocalActivity
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.poulastaa.core.presentation.Email
import com.poulastaa.core.presentation.KyokuWindowSize
import com.poulastaa.core.presentation.designsystem.ObserveAsEvent

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
internal fun EmailSingUpRootScreen(
    email: Email?,
    viewmodel: EmailSingUpViewmodel = hiltViewModel(),
    navigateBack: () -> Unit
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

        },
        expandedSmallContent = {

        },
        expandedCompactContent = {

        },
        expandedLargeContent = {

        }
    )
}