package com.poulastaa.kyoku.presentation.screen.home_root

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.poulastaa.kyoku.data.model.home_nav_drawer.HomeRootUiEvent
import com.poulastaa.kyoku.data.model.home_nav_drawer.Nav
import com.poulastaa.kyoku.data.model.home_nav_drawer.NavDrawerUserInfo
import com.poulastaa.kyoku.data.model.screens.auth.UiEvent
import com.poulastaa.kyoku.navigation.Screens
import com.poulastaa.kyoku.navigation.navigateWithData
import com.poulastaa.kyoku.presentation.screen.home_root.home.HomeContainer
import com.poulastaa.kyoku.presentation.screen.home_root.home.component.HomeScreenBottomBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeRootDrawer(
    viewModel: HomeRootViewModel = hiltViewModel(),
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
    navController: NavHostController = rememberNavController(),
    context: Context = LocalContext.current,
    scope: CoroutineScope = rememberCoroutineScope(),
    navigate: (UiEvent) -> Unit
) {
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


    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
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
                        opnDrawer = {
                            scope.launch {
                                drawerState.open()
                            }
                        },
                        state = viewModel.state,
                        navigateWithUiEvent = viewModel::onEvent,
                        nav = viewModel::onEvent
                    )
            }
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .background(color = Color.Transparent)
                .navigationBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .background(color = MaterialTheme.colorScheme.tertiary)
                    .navigationBarsPadding()
                    .clickable {
                        scope.launch {
                            Toast
                                .makeText(
                                    context,
                                    "click",
                                    Toast.LENGTH_SHORT
                                )
                                .show()
                        }
                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Player",
                    color = MaterialTheme.colorScheme.background,
                    fontSize = MaterialTheme.typography.titleLarge.fontSize
                )
            }


            if (viewModel.state.nav != Nav.NON)
                HomeScreenBottomBar(
                    isHome = viewModel.state.nav == Nav.HOME
                ) {
                    viewModel.onEvent(
                        HomeRootUiEvent.BottomNavClick(it)
                    )
                }
        }
    }
}