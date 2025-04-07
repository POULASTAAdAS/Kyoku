package com.poulastaa.core.database.model

import com.poulastaa.core.domain.model.PlaylistId

data class PayloadPlaylistPoster(
    val poster: String,
    val playlistId: PlaylistId,
)
