package com.poulastaa.kyoku.data.model.screens.common

enum class ItemsType(val title: String) {
    PLAYLIST(title = "Playlist"),

    ALBUM(title = "Album"),
    ALBUM_PREV(title = "AlbumPrev"),

    ARTIST(title = "Artist"),
    ARTIST_MIX(title = "ArtistMix"),
    DAILY_MIX(title = "DailyMix"),
    ARTIST_MORE(title = "ArtistMore"),

    FAVOURITE(title = "Favourite"),

    SONG(title = "Song"),

    HISTORY(title = "History"),

    ERR(title = "Err")
}