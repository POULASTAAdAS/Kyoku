package com.poulastaa.core.presentation.designsystem.model

import com.poulastaa.core.domain.model.AlbumId

data class UiPrevAlbum(
    val id: AlbumId = -1,
    val name: String = "",
    val poster: String? = null,
)