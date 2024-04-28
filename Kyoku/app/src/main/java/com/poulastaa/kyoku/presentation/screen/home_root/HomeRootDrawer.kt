package com.poulastaa.kyoku.presentation.screen.home_root

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.poulastaa.kyoku.data.model.UiEvent
import com.poulastaa.kyoku.data.model.home_nav_drawer.NavDrawerUserInfo
import com.poulastaa.kyoku.navigation.Screens
import com.poulastaa.kyoku.navigation.navigateWithData
import com.poulastaa.kyoku.presentation.screen.home_root.home.HomeContainer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeRootDrawer(
    viewModel: HomeRootViewModel = hiltViewModel(),
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
    navController: NavHostController = rememberNavController(),
    sheetState: SheetState = rememberModalBottomSheetState(),
    context: Context = LocalContext.current,
    scope: CoroutineScope = rememberCoroutineScope(),
    changeThem: () -> Unit
) {
    LaunchedEffect(key1 = Unit) {
        viewModel.loadSongIfAny()
    }

    LaunchedEffect(key1 = viewModel.uiEvent) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.Navigate -> {
                    if (event.route == Screens.Library.route ||
                        event.route == Screens.Home.route
                    ) {
                        navController.popBackStack()
                        navController.navigate(event.route)
                    } else navController.navigate(event.route)

                    drawerState.close()
                }

                is UiEvent.NavigateWithData -> {
                    navController.navigateWithData(event)
                    drawerState.close()
                }

                else -> Unit
            }
        }
    }


    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = drawerState.isOpen,
        drawerContent = {
            HomeDrawerContent(
                navDrawerUserInfo = NavDrawerUserInfo(
                    imageUrl = viewModel.state.profilePicUrl,
                    userName = viewModel.state.userName,
                    isCookie = viewModel.state.isCookie,
                    headerValue = viewModel.state.headerValue
                ),
                navigate = viewModel::onEvent
            )
        },
        content = {
            if (viewModel.state.isLoading)
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                }
            else
                HomeContainer(
                    profileUrl = viewModel.state.profilePicUrl,
                    isCookie = viewModel.state.isCookie,
                    authHeader = viewModel.state.headerValue,
                    navController = navController,
                    state = viewModel.state,
                    scope = scope,
                    opnDrawer = {
                        scope.launch {
                            drawerState.open()
                        }
                    },
                    playControl = viewModel::onEvent,
                    navigateWithUiEvent = viewModel::onEvent
                )
        }
    )
}