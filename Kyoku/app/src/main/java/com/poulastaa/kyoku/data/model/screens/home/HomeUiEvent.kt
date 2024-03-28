package com.poulastaa.kyoku.data.model.screens.home

sealed class HomeUiEvent {
    data class EmitToast(val message: String) : HomeUiEvent()
    data object SomethingWentWrong : HomeUiEvent()
    data class ItemClick(
        val type: HomeScreenItemType,
        val id: Long? = null,
        val name: String? = null
    ) : HomeUiEvent()
}

enum class HomeScreenItemType {
    PLAYLIST,
    ALBUM,
    ALBUM_PREV,
    ARTIST,
    ARTIST_MIX,
    FAVOURITE,
    SONG,
    ARTIST_MORE,
    HISTORY
}

