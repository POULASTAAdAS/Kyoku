package com.poulastaa.network.model

import com.poulastaa.core.data.model.ArtistDto
import com.poulastaa.core.data.model.PreArtistSongDto
import com.poulastaa.core.data.model.PrevAlbumDto
import com.poulastaa.core.data.model.PrevSongDto
import com.poulastaa.core.domain.model.ResponseStatus
import kotlinx.serialization.Serializable

@Serializable
data class NewHomeDto(
    val status: ResponseStatus = ResponseStatus.FAILURE,
    val popularSongMixPrev: List<PrevSongDto> = emptyList(),
    val popularSongFromYourTimePrev: List<PrevSongDto> = emptyList(),
    val favouriteArtistMixPrev: List<PrevSongDto> = emptyList(),
    val dayTypeSong: List<PrevSongDto> = emptyList(),
    val popularAlbum: List<PrevAlbumDto> = emptyList(),
    val popularArtist: List<ArtistDto> = emptyList(),
    val popularArtistSong: List<PreArtistSongDto> = emptyList(),
)