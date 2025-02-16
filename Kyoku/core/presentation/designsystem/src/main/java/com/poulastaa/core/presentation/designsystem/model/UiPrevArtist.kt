package com.poulastaa.core.presentation.designsystem.model

import com.poulastaa.core.domain.model.ArtistId

data class UiPrevArtist(
    val id: ArtistId = -1,
    val name: String = "",
    val cover: String? = null,
)
