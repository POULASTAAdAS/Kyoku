package com.poulastaa.kyoku.presentation.screen.home_root

import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.poulastaa.kyoku.data.model.home_nav_drawer.NavDrawerUserInfo
import com.poulastaa.kyoku.data.model.screens.auth.UiEvent
import com.poulastaa.kyoku.navigation.home_navigation.SetupHomeRootNavGraph
import kotlinx.coroutines.launch

@Composable
fun HomeRootScreen(
    viewModel: HomeRootViewModel = hiltViewModel(),
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
    homeNavController: NavHostController = rememberNavController()
) {
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = viewModel.uiEvent) {
        viewModel.uiEvent.collect {
            when (it) {
                is UiEvent.Navigate -> {
                    homeNavController.navigate(it.route)
                    drawerState.close()
                }

                is UiEvent.ShowToast -> Unit
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
            SetupHomeRootNavGraph(
                homeRootUiState = viewModel.state,
                navHostController = homeNavController,
                opnDrawer = {
                    scope.launch {
                        drawerState.open()
                    }
                },
                handleUiEvent = viewModel::onEvent
            )
        }
    )
}