package com.poulastaa.kyoku.presentation.screen.home_root.home

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import com.poulastaa.kyoku.data.model.api.service.home.HomeType
import com.poulastaa.kyoku.data.model.home_nav_drawer.HomeRootUiEvent
import com.poulastaa.kyoku.data.model.screens.auth.UiEvent

@OptIn(ExperimentalMaterial3Api::class)
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

                is UiEvent.NavigateWithData -> {
                    navigate.invoke(
                        HomeRootUiEvent.NavigateWithData(
                            route = event.route,
                            type = event.itemsType,
                            songType = event.songType,
                            id = event.id,
                            name = event.name,
                            longClickType = event.longClickType,
                            isApiCall = event.isApiCall,
                            isPlay = event.isPlay
                        )
                    )
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
        viewModel.state.data.fevArtistMixPrevUrls.isEmpty() ||
        viewModel.state.data.artistPrev.isEmpty()
    ) HomeScreenContentLoading(paddingValues, isSmallPhone)
    else {
        when (viewModel.state.dataType) {
            HomeType.NEW_USER_REQ -> {
                HomeScreenContentNewUser(
                    paddingValues = paddingValues,
                    isSmallPhone = isSmallPhone,
                    isInternetError = viewModel.state.isInternetError,
                    errorMessage = viewModel.state.errorMessage,
                    isCookie = isCookie,
                    headerValue = authHeader,
                    bottomSheetState = viewModel.state.isBottomSheetOpen,
                    isBottomSheetLoading = viewModel.state.isBottomSheetLoading,
                    data = viewModel.state.data,
                    bottomSheetData = viewModel.state.bottomSheetData,
                    onClick = viewModel::onEvent,
                    onLongClick = viewModel::onEvent
                )
            }

            else -> {
                if (viewModel.state.data.dailyMixPrevUrls.isEmpty())
                    HomeScreenContentNewUser(
                        paddingValues = paddingValues,
                        isSmallPhone = isSmallPhone,
                        isCookie = isCookie,
                        headerValue = authHeader,
                        errorMessage = viewModel.state.errorMessage,
                        isInternetError = viewModel.state.isInternetError,
                        data = viewModel.state.data,
                        bottomSheetData = viewModel.state.bottomSheetData,
                        bottomSheetState = viewModel.state.isBottomSheetOpen,
                        isBottomSheetLoading = viewModel.state.isBottomSheetLoading,
                        onClick = viewModel::onEvent,
                        onLongClick = viewModel::onEvent
                    )
                else
                    HomeScreenContentOldUser(
                        paddingValues = paddingValues,
                        isSmallPhone = isSmallPhone,
                        data = viewModel.state.data,
                        bottomSheetData = viewModel.state.bottomSheetData,
                        isInternetError = viewModel.state.isInternetError,
                        errorMessage = viewModel.state.errorMessage,
                        isCookie = isCookie,
                        headerValue = authHeader,
                        bottomSheetState = viewModel.state.isBottomSheetOpen,
                        isBottomSheetLoading = viewModel.state.isBottomSheetLoading,
                        onClick = viewModel::onEvent,
                        onLongClick = viewModel::onEvent
                    )
            }
        }
    }
}

