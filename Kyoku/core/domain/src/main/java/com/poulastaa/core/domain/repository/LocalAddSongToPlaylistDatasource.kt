package com.poulastaa.core.domain.repository

import com.poulastaa.core.domain.model.ArtistId
import com.poulastaa.core.domain.model.DtoPrevArtist
import com.poulastaa.core.domain.model.DtoSong
import com.poulastaa.core.domain.model.PlaylistId

interface LocalAddSongToPlaylistDatasource {
    suspend fun saveSong(playlistId: PlaylistId, song: DtoSong)
    suspend fun loadPlaylistSongIdList(playlistId: PlaylistId): List<PlaylistId>

    suspend fun getArtist(artistId: ArtistId): DtoPrevArtist?
}