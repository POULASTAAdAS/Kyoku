package com.poulastaa.play.presentation.explore_artist

sealed interface ExploreArtistUiEvent {
    data class OnItemClick(val id: Long, val type: Type) : ExploreArtistUiEvent

    sealed interface ThreeDotEvent : ExploreArtistUiEvent {
        data class OnThreeDotOpen(val id: Long, val type: Type) : ThreeDotEvent
        data class OnThreeDotClose(val id: Long, val type: Type) : ThreeDotEvent
        data class OnThreeDotEventClick(
            val id: Long,
            val type: ExploreArtistThreeDotEvent
        ) : ThreeDotEvent
    }

    enum class Type {
        SONG,
        ALBUM
    }
}

sealed interface ExploreArtistThreeDotEvent

enum class AlbumThreeDotEvent(val value: String) : ExploreArtistThreeDotEvent {
    PLAY("play"),
    SAVE_ALBUM("save album")
}

enum class SongThreeDotEvent(val value: String) : ExploreArtistThreeDotEvent {
    PLAY("play"),
    PLAY_NEXT("play_next"),
    PLAY_LAST("play_last"),
    ADD_TO_PLAYLIST("add_to_playlist"),
    ADD_TO_FAVOURITE("add_to_favourite"),
    VIEW_ARTISTS("view_artists")
}