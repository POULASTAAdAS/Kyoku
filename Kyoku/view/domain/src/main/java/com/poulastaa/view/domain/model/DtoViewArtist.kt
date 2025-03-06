package com.poulastaa.view.domain.model

import com.poulastaa.core.domain.model.ArtistId

data class DtoViewArtist(
    val id: ArtistId = -1,
    val name: String = "",
    val cover: String = "",
    val isFollowing: Boolean = false,
)