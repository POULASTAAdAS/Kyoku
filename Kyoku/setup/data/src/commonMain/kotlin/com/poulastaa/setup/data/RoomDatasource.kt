package com.poulastaa.setup.data

import com.poulastaa.common.database.dao.PlaylistDao
import com.poulastaa.common.database.dao.SongDao
import com.poulastaa.common.database.mapper.toBundle
import com.poulastaa.common.database.mapper.toEntity
import com.poulastaa.common.domain.model.DtoFullPlaylist
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
    override suspend fun savePlaylist(playlist: DtoFullPlaylist): EmptyResponse<DataError> = safeDbCall {
        songDao.upsertFullSongs(playlist.songs.map { it.toBundle() })
        playlistDao.replacePlaylist(
            playlist = playlist.playlist.toEntity(),
            songIds = playlist.songs.map { it.id },
            addedAt = Clock.System.now().toEpochMilliseconds(),
        )
    }
}
