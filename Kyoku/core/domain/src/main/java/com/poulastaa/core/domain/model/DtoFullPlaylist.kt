package com.poulastaa.core.domain.model

import com.poulastaa.core.domain.utils.InternalId
import com.poulastaa.core.domain.utils.PlaylistId

data class DtoFullPlaylist(
    val internalId: InternalId = -1,
    val playlistId: PlaylistId,
    val title: String,
    val songs: List<DtoSong>,
)
