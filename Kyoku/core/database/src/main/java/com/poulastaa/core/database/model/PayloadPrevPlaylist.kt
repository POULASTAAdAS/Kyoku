package com.poulastaa.core.database.model

import com.poulastaa.core.domain.model.PlaylistId

data class PayloadPrevPlaylist(
    val id: PlaylistId = -1,
    val title: String = "",
    val poster: String? = null,
)
