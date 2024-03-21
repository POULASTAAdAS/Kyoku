package com.poulastaa.kyoku.presentation.screen.home_root.home

import android.widget.Toast
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import com.poulastaa.kyoku.data.model.api.service.home.HomeType
import com.poulastaa.kyoku.data.model.home_nav_drawer.HomeRootUiEvent
import com.poulastaa.kyoku.data.model.screens.auth.UiEvent
import com.poulastaa.kyoku.navigation.Screens
import com.poulastaa.kyoku.presentation.screen.home_root.home.component.HomeTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    title: String,
    profileUrl: String,
    isCookie: Boolean,
    authHeader: String,
    viewModel: HomeScreenViewModel,
    opnDrawer: () -> Unit,
    navigate: (HomeRootUiEvent) -> Unit
) {
    val context = LocalContext.current
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val isSmallPhone = LocalConfiguration.current.screenWidthDp <= 411

    LaunchedEffect(key1 = viewModel.uiEvent) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.Navigate -> {

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

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            HomeTopAppBar(
                title = title,
                profileUrl = profileUrl,
                isCookie = isCookie,
                authHeader = authHeader,
                onProfileClick = opnDrawer,
                scrollBehavior = scrollBehavior,
                isSmallPhone = isSmallPhone,
                onSearchClick = {
                    navigate.invoke(HomeRootUiEvent.Navigate(Screens.Search.route))
                }
            )
        },
        bottomBar = {

        }
    ) {
        if (
            viewModel.state.isLoading ||
            viewModel.state.data.albumPrev.isEmpty() ||
            viewModel.state.data.fevArtistMixPrev.isEmpty() ||
            viewModel.state.data.artistPrev.isEmpty()
        ) HomeScreenContentLoading(it, isSmallPhone)
        else {
            when (viewModel.state.dataType) {
                HomeType.NEW_USER_REQ -> {
                    HomeScreenContentNewUser(
                        paddingValues = it,
                        isSmallPhone = isSmallPhone,
                        data = viewModel.state.data,

                        isInternetError = viewModel.state.isInternetError,
                        errorMessage = viewModel.state.errorMessage,
                        isCookie = isCookie,
                        headerValue = authHeader
                    )
                }

                else -> {
                    if (!viewModel.state.data.favourites &&
                        viewModel.state.data.playlist.isEmpty()
                    ) HomeScreenContentNewUser(
                        paddingValues = it,
                        isSmallPhone = isSmallPhone,
                        data = viewModel.state.data,

                        isInternetError = viewModel.state.isInternetError,
                        errorMessage = viewModel.state.errorMessage,
                        isCookie = isCookie,
                        headerValue = authHeader
                    ) else
                        HomeScreenContentOldUser(
                            paddingValues = it,
                            isSmallPhone = isSmallPhone,
                            data = viewModel.state.data,

                            isInternetError = viewModel.state.isInternetError,
                            errorMessage = viewModel.state.errorMessage,
                            isCookie = isCookie,
                            headerValue = authHeader
                        )
                }
            }
        }
    }
}

