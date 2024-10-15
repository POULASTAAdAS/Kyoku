package com.poulastaa.play.presentation.profile

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.poulastaa.core.presentation.designsystem.AppThem
import com.poulastaa.core.presentation.ui.ObserveAsEvent

@Composable
fun ProfileLandscapeRootScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    navigateToLibrary: () -> Unit,
    navigateBack: () -> Unit,
) {
    ObserveAsEvent(viewModel.uiState) {
        when (it) {
            is ProfileUiAction.NavigateToLibrary -> navigateToLibrary()
        }
    }

    ProfileLandscapeScreen(
        state = viewModel.state,
        navigateBack = navigateBack,
        onEvent = viewModel::onEvent
    )
}

@Composable
private fun ProfileLandscapeScreen(
    state: ProfileUiState,
    onEvent: (ProfileUiEvent) -> Unit,
    navigateBack: () -> Unit,
) {

}

@Preview
@Composable
private fun Preview() {
    AppThem {
        ProfileLandscapeScreen(
            state = ProfileUiState(
                name = "Poulastaa Das"
            ),
            onEvent = {},
            navigateBack = {}
        )
    }
}