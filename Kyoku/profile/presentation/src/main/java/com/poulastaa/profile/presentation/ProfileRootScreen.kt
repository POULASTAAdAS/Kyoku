package com.poulastaa.profile.presentation

import android.app.Activity
import android.widget.Toast
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.poulastaa.core.presentation.designsystem.KyokuWindowSize
import com.poulastaa.core.presentation.designsystem.ObserveAsEvent
import com.poulastaa.profile.domain.model.ProfileAllowedNavigationScreen
import com.poulastaa.profile.presentation.components.UpdateUsernameBottomSheet

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ProfileRootScreen(
    navigate: (ProfileAllowedNavigationScreen) -> Unit,
    navigateBack: () -> Unit,
) {
    val viewmodel = hiltViewModel<ProfileViewmodel>()

    val context = LocalContext.current as Activity
    val windowSizeClass = calculateWindowSizeClass(context)

    val state by viewmodel.state.collectAsStateWithLifecycle()

    val bottomSheet = rememberModalBottomSheetState(
        confirmValueChange = {
            state.isMakingApiCall.not()
        }
    )

    ObserveAsEvent(viewmodel.uiEvent) { action ->
        when (action) {
            is ProfileUiEvent.EmitToast -> Toast.makeText(
                context,
                action.message.asString(context),
                Toast.LENGTH_LONG
            ).show()

            is ProfileUiEvent.Navigate -> navigate(action.screen)
        }
    }

    LaunchedEffect(state.isUpdateUsernameBottomSheetVisible) {
        if (state.isUpdateUsernameBottomSheetVisible) bottomSheet.show()
        else if (state.isMakingApiCall.not()) bottomSheet.hide()
    }

    KyokuWindowSize(
        windowSizeClass = windowSizeClass,
        compactContent = {
            ProfileCompactScreen(
                state = state,
                onAction = viewmodel::onAction,
                navigateBack = navigateBack
            )
        },
        mediumContent = {
            ProfileCompactScreen(
                state = state,
                onAction = viewmodel::onAction,
                navigateBack = navigateBack
            )
        },
        expandedContent = {
            ProfileExpandedScreen(
                state = state,
                onAction = viewmodel::onAction,
                navigateBack = navigateBack
            )
        }
    )

    if (bottomSheet.isVisible) UpdateUsernameBottomSheet(
        username = state.username,
        isMakingApiCall = state.isMakingApiCall,
        onAction = viewmodel::onAction
    )
}