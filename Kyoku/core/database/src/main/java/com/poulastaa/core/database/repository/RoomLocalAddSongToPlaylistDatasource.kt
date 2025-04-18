package com.poulastaa.core.database.repository

import com.poulastaa.core.database.dao.RootDao
import com.poulastaa.core.database.dao.ViewDao
import com.poulastaa.core.database.entity.EntityRelationSongPlaylist
import com.poulastaa.core.database.mapper.toDtoPrevArtist
import com.poulastaa.core.domain.model.ArtistId
import com.poulastaa.core.domain.model.DtoPrevArtist
import com.poulastaa.core.domain.model.DtoSong
import com.poulastaa.core.domain.model.PlaylistId
import com.poulastaa.core.domain.repository.LocalAddSongToPlaylistDatasource
import javax.inject.Inject

internal class RoomLocalAddSongToPlaylistDatasource @Inject constructor(
    private val rootDao: RootDao,
    private val viewDao: ViewDao,
    private val common: RoomCommonInsertDatasource,
) : LocalAddSongToPlaylistDatasource {
    override suspend fun saveSong(
        playlistId: PlaylistId,
        song: DtoSong,
    ) {
        common.insertSong(listOf(song)).also {
            rootDao.insertRelationSongPlaylist(
                EntityRelationSongPlaylist(
                    songId = song.id,
                    playlistId = playlistId,
                )
            )
        }
    }

    override suspend fun loadPlaylistSongIdList(
        playlistId: PlaylistId,
    ): List<PlaylistId> = viewDao.getPlaylistPrevSongs(playlistId).map { it.id }

    override suspend fun getArtist(artistId: ArtistId): DtoPrevArtist? =
        rootDao.getArtist(artistId)?.toDtoPrevArtist()
            ?: rootDao.getPrevArtist(artistId)?.toDtoPrevArtist()
}