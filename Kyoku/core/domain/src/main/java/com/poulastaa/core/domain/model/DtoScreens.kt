package com.poulastaa.core.domain.model

sealed interface DtoScreens {
    data class ViewArtist(val artistId: ArtistId) : DtoScreens
}