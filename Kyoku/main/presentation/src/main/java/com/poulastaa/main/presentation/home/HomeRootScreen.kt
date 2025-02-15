package com.poulastaa.main.presentation.home

import android.app.Activity
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.poulastaa.core.presentation.designsystem.KyokuWindowSize
import com.poulastaa.core.presentation.designsystem.model.UiUser

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
internal fun HomeRootScreen(
    viewmodel: HomeViewmodel,
    user: UiUser,
    toggleDrawer: () -> Unit,
) {
    val activity = LocalContext.current as Activity
    val windowSizeClass = calculateWindowSizeClass(activity)

    KyokuWindowSize(
        windowSizeClass = windowSizeClass,
        compactContent = {
            HomeCompactScreen(
                user = user,
                toggleDrawer = toggleDrawer
            )
        },
        mediumContent = {
            HomeCompactScreen(
                user = user,
                toggleDrawer = toggleDrawer
            )
        },
        expandedContent = {
            HomeCompactScreen(
                user = user,
                toggleDrawer = toggleDrawer
            )
        }
    )
}