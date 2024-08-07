package com.poulastaa.auth.network.res

import com.poulastaa.core.data.model.ArtistDto
import com.poulastaa.core.data.model.PreArtistSongDto
import com.poulastaa.core.data.model.PrevAlbumDto
import com.poulastaa.core.data.model.PrevSongDto
import kotlinx.serialization.Serializable

@Serializable
data class LogInDto(
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