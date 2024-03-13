package com.poulastaa.kyoku.data.model.home_nav_drawer

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.poulastaa.kyoku.navigation.Screens

data class HomeNavDrawerItem(
    val label: String,
    val icon: ImageVector,
    val onClick: HomeRootUiEvent
)

object HomeNavDrawerItemList {
    val list = arrayListOf(
        HomeNavDrawerItem(
            label = "H I S T O R Y",
            icon = Icons.Rounded.DateRange,
            onClick = HomeRootUiEvent.Navigate(Screens.History.route)
        ),
        HomeNavDrawerItem(
            label = "S E T T I N G S",
            icon = Icons.Rounded.Settings,
            onClick = HomeRootUiEvent.Navigate(Screens.Settings.route)
        )
    )
}

