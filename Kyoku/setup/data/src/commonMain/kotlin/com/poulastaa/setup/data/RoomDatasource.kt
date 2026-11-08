package com.poulastaa.setup.data

import com.poulastaa.common.database.dao.PlaylistDao
import com.poulastaa.common.database.dao.SongDao
import com.poulastaa.common.database.mapper.toBundle
import com.poulastaa.common.database.mapper.toDto
import com.poulastaa.common.database.mapper.toEntity
import com.poulastaa.common.domain.model.DtoFullPlaylist
import com.poulastaa.common.domain.result.AppResult
import com.poulastaa.common.domain.result.DataError
import com.poulastaa.common.domain.result.EmptyResponse
import com.poulastaa.common.domain.result.safeDbCall
import com.poulastaa.setup.domain.SetupLocalDatasource
import org.koin.core.annotation.Single
import kotlin.time.Clock

@Single(binds = [SetupLocalDatasource::class])
class RoomDatasource(
    private val songDao: SongDao,
    private val playlistDao: PlaylistDao,
) : SetupLocalDatasource {
    override suspend fun isSpotifyPlaylistImported(
        spotifyPlaylistId: String,
    ): AppResult<Boolean, DataError> = safeDbCall {
        playlistDao.isSpotifyPlaylistImported(spotifyPlaylistId)
    }

    override suspend fun savePlaylist(
        playlist: DtoFullPlaylist,
        spotifyPlaylistId: String,
    ): EmptyResponse<DataError> = safeDbCall {
        songDao.upsertFullSongs(playlist.songs.map { it.toBundle() })
        playlistDao.replacePlaylist(
            playlist = playlist.playlist.toEntity(spotifyPlaylistId),
            songIds = playlist.songs.map { it.id },
            addedAt = Clock.System.now().toEpochMilliseconds(),
        )
    }

    override suspend fun getPlaylists(): AppResult<List<DtoFullPlaylist>, DataError> = safeDbCall {
        playlistDao.getPlaylists().map { playlist ->
            DtoFullPlaylist(
                playlist = playlist.toDto(),
                songs = playlistDao.getPlaylistSongs(playlist.id).map { it.toDto() },
            )
        }
    }
}
