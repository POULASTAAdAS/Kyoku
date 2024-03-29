package com.poulastaa.kyoku.data.model.screens.auth

import com.poulastaa.kyoku.data.model.screens.common.ItemsType
import com.poulastaa.kyoku.navigation.Screens

sealed class UiEvent {
    data class Navigate(val route: String) : UiEvent()

    data class NavigateWithData(
        val route: String = Screens.Home.route,
        val type: ItemsType = ItemsType.PLAYLIST,
        val id: Long = -1,
        val name: String = "name"
    ) : UiEvent()

    data class ShowToast(val message: String) : UiEvent()
}
