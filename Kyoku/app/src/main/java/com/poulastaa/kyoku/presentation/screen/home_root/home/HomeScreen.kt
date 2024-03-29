package com.poulastaa.kyoku.presentation.screen.home_root.home

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import com.poulastaa.kyoku.data.model.api.service.home.HomeType
import com.poulastaa.kyoku.data.model.home_nav_drawer.HomeRootUiEvent
import com.poulastaa.kyoku.data.model.screens.auth.UiEvent

@Composable
fun HomeScreen(
    isCookie: Boolean,
    authHeader: String,
    viewModel: HomeScreenViewModel = hiltViewModel(),
    isSmallPhone: Boolean,
    context: Context,
    paddingValues: PaddingValues,
    navigate: (HomeRootUiEvent) -> Unit
) {
    LaunchedEffect(key1 = viewModel.state.isInternetAvailable) {
        viewModel.loadStartupData(context)
    }

    LaunchedEffect(key1 = viewModel.uiEvent) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.Navigate -> {
                    navigate.invoke(HomeRootUiEvent.Navigate(event.route))
                }

                is UiEvent.ShowToast -> {
                    Toast.makeText(
                        context,
                        event.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    if (
        viewModel.state.isLoading ||
        viewModel.state.data.albumPrev.isEmpty() ||
        viewModel.state.data.fevArtistMixPrev.isEmpty() ||
        viewModel.state.data.artistPrev.isEmpty()
    ) HomeScreenContentLoading(paddingValues, isSmallPhone)
    else {
        when (viewModel.state.dataType) {
            HomeType.NEW_USER_REQ -> {
                HomeScreenContentNewUser(
                    paddingValues = paddingValues,
                    isSmallPhone = isSmallPhone,
                    data = viewModel.state.data,

                    isInternetError = viewModel.state.isInternetError,
                    errorMessage = viewModel.state.errorMessage,
                    isCookie = isCookie,
                    headerValue = authHeader,
                    onClick = viewModel::onEvent
                )
            }

            else -> {
                if (!viewModel.state.data.favourites &&
                    viewModel.state.data.playlist.isEmpty()
                ) HomeScreenContentNewUser(
                    paddingValues = paddingValues,
                    isSmallPhone = isSmallPhone,
                    data = viewModel.state.data,

                    isInternetError = viewModel.state.isInternetError,
                    errorMessage = viewModel.state.errorMessage,
                    isCookie = isCookie,
                    headerValue = authHeader,
                    onClick = viewModel::onEvent
                ) else
                    HomeScreenContentOldUser(
                        paddingValues = paddingValues,
                        isSmallPhone = isSmallPhone,
                        data = viewModel.state.data,
                        isInternetError = viewModel.state.isInternetError,
                        errorMessage = viewModel.state.errorMessage,
                        isCookie = isCookie,
                        headerValue = authHeader,
                        onClick = viewModel::onEvent
                    )
            }
        }
    }
}

