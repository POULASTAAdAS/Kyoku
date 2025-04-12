package com.poulastaa.add.network.model

import com.poulastaa.core.domain.model.PlaylistId
import com.poulastaa.core.domain.model.SongId
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

@Serializable
@InternalSerializationApi
internal data class AddSongToPlaylistRequest(
    val playlistId: PlaylistId,
    val songId: SongId,
)
