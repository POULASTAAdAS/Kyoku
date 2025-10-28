package com.poulastaa.board.domain.import_playlist.model

import com.poulastaa.core.domain.model.DtoPrevSong
import com.poulastaa.core.domain.utils.InternalId
import com.poulastaa.core.domain.utils.PlaylistId

data class DtoPrevPlaylist(
    val internalId: InternalId,
    val playlistId: PlaylistId,
    val name: String,
    val songs: List<DtoPrevSong>,
)
