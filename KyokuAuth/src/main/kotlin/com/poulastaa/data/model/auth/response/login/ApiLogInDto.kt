package com.poulastaa.data.model.auth.response.login

import com.poulastaa.data.model.auth.response.UserAuthStatus
import kotlinx.serialization.Serializable

@Serializable
data class ApiLogInDto(
    val status: UserAuthStatus = UserAuthStatus.USER_NOT_FOUND,
    val popularSongMixPrev: List<PrevSongDto> = emptyList(),
    val popularSongFromYourTimePrev: List<PrevSongDto> = emptyList(),
    val favouriteArtistMixPrev: List<PrevSongDto> = emptyList(),
    val dayTypeSong: List<PrevSongDto> = emptyList(),
    val popularAlbum: List<PrevAlbumDto> = emptyList(),
    val popularArtist: List<ArtistDto> = emptyList(),
    val popularArtistSong: List<PreArtistSongDto> = emptyList(),

    val savedPlaylist: List<PlaylistDto> = emptyList(),
    val savedAlbum: List<AlbumDto> = emptyList(),
    val savedArtist: List<ArtistDto> = emptyList(),

    val favouriteSong: List<SongDto> = emptyList(),
)