package com.poulastaa.core.domain.model

import com.poulastaa.core.domain.repository.PlaylistId
import com.poulastaa.core.domain.repository.SongId

data class DtoSyncPlaylistSongPayload(
    val removeIdList: List<Pair<PlaylistId, List<SongId>>>,
    val newData: List<Pair<PlaylistId, List<DtoSong>>>,
)
