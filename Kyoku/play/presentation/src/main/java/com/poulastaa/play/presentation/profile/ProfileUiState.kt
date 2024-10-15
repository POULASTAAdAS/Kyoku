package com.poulastaa.play.presentation.profile

data class ProfileUiState(
    val header:String = "",
    val name: String = "Poulastaa Das",
    val imageUrl: String = "",
    val savedArtistCount: Int = 0,
    val savedAlbumCount: Int = 0,
    val savedPlaylistCount: Int = 0,
)

enum class ProfileItemType {
    PLAYLIST,
    ALBUM,
    ARTIST,
    LIBRARY
}
