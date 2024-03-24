package com.poulastaa.kyoku.data.model.screens.home

sealed class HomeUiEvent {
    data class EmitToast(val message: String) : HomeUiEvent()
    data object SomethingWentWrong : HomeUiEvent()
    data class BottomNavClick(val bottomNav: HomeScreenBottomNavigation) : HomeUiEvent()
    data class ItemClick(val type: HomeScreenItemType, val id: Long) : HomeUiEvent()
}

enum class HomeScreenItemType {
    PLAYLIST,
    ARTIST,
    FAVOURITE,
    SONG,
    ARTIST_MORE,
    HISTORY
}

enum class HomeScreenBottomNavigation {
    HOME_SCREEN,
    LIBRARY_SCREEN
}
