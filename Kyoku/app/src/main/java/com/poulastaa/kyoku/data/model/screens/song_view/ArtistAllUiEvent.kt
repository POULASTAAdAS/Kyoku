package com.poulastaa.kyoku.data.model.screens.song_view

sealed class ArtistAllUiEvent {
    data class EmitToast(val message: String) : ArtistAllUiEvent()
    data object SomethingWentWrong : ArtistAllUiEvent()

    sealed class ItemClick : ArtistAllUiEvent() {
        data class AlbumClick(val id: Long , val name:String) : ItemClick()
        data class SongClick(val id: Long) : ItemClick()
    }

    data class ItemLongClick(
        val url: String,
        val id: Long,
        val name: String,
        val type: BottomSheetData.BottomSheetDataType
    ) : ArtistAllUiEvent()

    sealed class BottomSheetItemClick : ArtistAllUiEvent() {
        data class AlbumClick(
            val id: Long,
            val name: String,
            val type: AlbumType
        ) : BottomSheetItemClick()

        data class SongClick(
            val id: Long,
            val name: String,
            val type: AllArtistSongType
        ) : BottomSheetItemClick()

        data object CancelClick : BottomSheetItemClick()
    }
}

enum class AlbumType {
    PLAY_ALBUM,
    ADD_TO_LIBRARY_ALBUM,
    REMOVE_FROM_LIBRARY_ALBUM,
    ADD_AS_PLAYLIST,
    DOWNLOAD_ALBUM
}

enum class AllArtistSongType {
    PLAY_SONG,
    ADD_TO_FAVOURITE,
    REMOVE_FROM_FAVOURITE,
    ADD_TO_PLAYLIST
}

