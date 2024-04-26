package com.poulastaa.kyoku.data.model.screens.auth

import com.poulastaa.kyoku.data.model.home_nav_drawer.HomeRootUiEvent
import com.poulastaa.kyoku.data.model.home_nav_drawer.SearchType
import com.poulastaa.kyoku.data.model.screens.common.ItemsType
import com.poulastaa.kyoku.data.model.screens.home.SongType
import com.poulastaa.kyoku.navigation.Screens

sealed class UiEvent {
    data class Navigate(val route: String) : UiEvent()

    data class NavigateWithData(
        val route: String = Screens.Home.route,
        val itemsType: ItemsType = ItemsType.PLAYLIST,
        val songType: SongType = SongType.HISTORY_SONG,
        val searchType: SearchType = SearchType.ALL_SEARCH,
        val id: Long = -1,
        val name: String = "name",
        val longClickType: String = "longClickType",
        val isApiCall: Boolean = false,
        val isPlay: Boolean = false
    ) : UiEvent()

    data class Play(
        val songId: Long = -1,
        val otherId: Long = -1,
        val playType: HomeRootUiEvent.Play.PlayType
    ) : UiEvent()

    data class ShowToast(val message: String) : UiEvent()
}
