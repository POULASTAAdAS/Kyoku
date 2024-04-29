package com.poulastaa.kyoku.data.model.home_nav_drawer

import android.content.Context
import com.poulastaa.kyoku.data.model.UiEvent
import com.poulastaa.kyoku.data.model.screens.common.ItemsType
import com.poulastaa.kyoku.data.model.screens.home.SongType
import com.poulastaa.kyoku.navigation.Screens


sealed class HomeRootUiEvent {
    data class EmitToast(val message: String) : HomeRootUiEvent()
    data object SomethingWentWrong : HomeRootUiEvent()

    data class Navigate(val route: String) : HomeRootUiEvent()

    data class NavigateWithData(
        val route: String = Screens.Home.route,
        val type: ItemsType = ItemsType.PLAYLIST,
        val songType: SongType = SongType.API_CALL,
        val searchType: SearchType = SearchType.ALL_SEARCH,
        val id: Long = -1,
        val name: String = "name",
        val longClickType: String = "longClickType",
        val isApiCall: Boolean = false,
        val isPlay: Boolean = false
    ) : HomeRootUiEvent()

    data class UpdateNav(val screens: Screens) : HomeRootUiEvent()

    data class Play(
        val context: Context,
        val songId: Long = -1,
        val otherId: Long = -1,
        val playType: UiEvent.PlayType,
        val isDarkThem: Boolean = true,
    ) : HomeRootUiEvent()

    sealed class PlayerUiEvent : HomeRootUiEvent() {
        data object SmallPlayerClick : PlayerUiEvent()
        data object CancelPlay : PlayerUiEvent()

        data object PlayPause : PlayerUiEvent()

        data class SelectedSongChange(val index: Int) : PlayerUiEvent()
        data object Backward : PlayerUiEvent()
        data object Forward : PlayerUiEvent()
        data class SeekTo(val index: Float) : PlayerUiEvent()
        data object SeekToPrev : PlayerUiEvent()
        data object SeekToNext : PlayerUiEvent()
        data object Stop : PlayerUiEvent()

        data class UpdateProgress(val value: Float) : PlayerUiEvent()
    }

    data class BottomNavClick(val bottomNav: HomeScreenBottomNavigation) : HomeRootUiEvent()
    data object LogOut : HomeRootUiEvent()
}

enum class HomeScreenBottomNavigation {
    HOME_SCREEN,
    LIBRARY_SCREEN
}

enum class SearchType {
    ALL_SEARCH,
    LIBRARY_SEARCH
}