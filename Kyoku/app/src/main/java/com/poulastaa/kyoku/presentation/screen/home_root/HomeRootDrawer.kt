package com.poulastaa.kyoku.presentation.screen.home_root

import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.poulastaa.kyoku.navigation.Screens
import com.poulastaa.kyoku.presentation.screen.home_root.home.HomeContainer
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeRootDrawer(
    viewModel: HomeRootViewModel = hiltViewModel(),
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
    homeNavController: NavHostController = rememberNavController(),
    navigate: (UiEvent) -> Unit
) {
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = viewModel.uiEvent) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.Navigate -> {
                    if (
                        event.route == Screens.Library.route ||
                        event.route == Screens.Home.route
                    ) {
                        homeNavController.popBackStack()
                        homeNavController.navigate(event.route)
                    } else navigate.invoke(event)

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
            HomeContainer(
                profileUrl = viewModel.state.profilePicUrl,
                isCookie = viewModel.state.isCookie,
                authHeader = viewModel.state.headerValue,
                navController = homeNavController,
                opnDrawer = {
                    scope.launch {
                        drawerState.open()
                    }
                },
                state = viewModel.state,
                navigateWithUiEvent = navigate,
                navigateWithHomeRoot = viewModel::onEvent
            )
        }
    )
}