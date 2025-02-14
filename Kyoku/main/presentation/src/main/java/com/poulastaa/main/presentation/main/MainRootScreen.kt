package com.poulastaa.main.presentation.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.poulastaa.core.domain.model.DtoScreens
import com.poulastaa.core.presentation.designsystem.gradiantBackground

@Composable
fun MainRootScreen(
    isInitial: Boolean,
    viewmodel: MainViewmodel = hiltViewModel(),
    navigate: (DtoScreens) -> Unit,
) {
    val nav = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerContent = {

        },
        gesturesEnabled = drawerState.isOpen
    ) {
        NavHost(
            navController = nav,
            startDestination = ScreensCore.Home
        ) {
            composable<ScreensCore.Home> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.linearGradient(
                                colors = gradiantBackground()
                            )
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Home",
                        fontWeight = FontWeight.Black,
                        fontSize = MaterialTheme.typography.headlineLarge.fontSize,
                        color = MaterialTheme.colorScheme.primaryContainer
                    )
                }
            }

            composable<ScreensCore.Library> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.linearGradient(
                                colors = gradiantBackground()
                            )
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Library",
                        fontWeight = FontWeight.Black,
                        fontSize = MaterialTheme.typography.headlineLarge.fontSize,
                        color = MaterialTheme.colorScheme.primaryContainer
                    )
                }
            }
        }
    }
}