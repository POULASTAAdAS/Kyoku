package com.poulastaa.data.model.home

import com.poulastaa.data.model.ArtistDto
import com.poulastaa.data.model.ResponseStatus
import kotlinx.serialization.Serializable

@Serializable
data class HomeDto(
    val status: ResponseStatus = ResponseStatus.FAILURE,
    val popularSongMixPrev: List<PrevSongDto> = emptyList(),
    val popularSongFromYourTimePrev: List<PrevSongDto> = emptyList(),
    val favouriteArtistMixPrev: List<PrevSongDto> = emptyList(),
    val dayTypeSong: List<PrevSongDto> = emptyList(),
    val popularAlbum: List<PrevAlbumDto> = emptyList(),
    val popularArtist: List<ArtistDto> = emptyList(),
    val popularArtistSong: List<PreArtistSongDto> = emptyList(),
)
