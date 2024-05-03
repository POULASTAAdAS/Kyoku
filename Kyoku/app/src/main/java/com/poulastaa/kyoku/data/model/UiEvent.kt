package com.poulastaa.kyoku.data.model

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
        val songIdList: List<Long> = emptyList(),
        val otherId: Long = -1,
        val playType: PlayType
    ) : UiEvent()

    data class ShowToast(val message: String) : UiEvent()

    enum class PlayType {
        HISTORY_SONG,
        ARTIST_SONG,
        PLAYLIST,
        PLAYLIST_SONG,
        ALBUM,
        ALBUM_SONG,
        ALBUM_PREV,
        ALBUM_PREV_SONG,
        ARTIST_MIX,
        ARTIST_MIX_SONG,
        DAILY_MIX,
        DAILY_MIX_SONG,
        FAVOURITE,
        FAVOURITE_SONG,
        ARTIST_MORE_ALL_SONG,
        ARTIST_MORE_ONE_SONG,
    }
}
