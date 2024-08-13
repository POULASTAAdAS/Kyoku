package com.poulastaa.core.database.repository

import com.poulastaa.core.database.dao.AddPlaylistDao
import com.poulastaa.core.database.dao.CommonDao
import com.poulastaa.core.database.entity.relation.SongPlaylistRelationEntity
import com.poulastaa.core.database.mapper.toPlaylistEntity
import com.poulastaa.core.database.mapper.toSongEntity
import com.poulastaa.core.domain.ExploreType
import com.poulastaa.core.domain.add_playlist.LocalAddPlaylistDatasource
import com.poulastaa.core.domain.model.PlaylistData
import com.poulastaa.core.domain.model.PrevSong
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class RoomLocalAddPlaylistDatasource @Inject constructor(
    private val commonDao: CommonDao,
    private val addPlaylistDao: AddPlaylistDao,
) : LocalAddPlaylistDatasource {
    override suspend fun getSongId(type: ExploreType): List<PrevSong> = when (type) {
        ExploreType.POPULAR -> addPlaylistDao.getPopularSongMix()
        ExploreType.OLD_GEM -> addPlaylistDao.getOldGem()
        ExploreType.ARTIST_MIX -> addPlaylistDao.getFavouriteArtistMix()
    }

    override suspend fun isNameConflict(name: String): Boolean =
        addPlaylistDao.isNameConflict(name).isNotEmpty()

    override suspend fun savePlaylist(playlist: PlaylistData): Unit = coroutineScope {
        val songEntry = playlist.listOfSong.map { it.toSongEntity() }
        val playlistEntry = playlist.toPlaylistEntity()

        val songDef = async { commonDao.insertSongs(songEntry) }
        val playlistDef = async { commonDao.insertPlaylist(playlistEntry) }

        songDef.await()
        playlistDef.await()

        songEntry.map { it.id }.map {
            SongPlaylistRelationEntity(
                songId = it,
                playlistId = playlistEntry.id
            )
        }.let {
            commonDao.insertSongPlaylistRelations(it)
        }
    }
}