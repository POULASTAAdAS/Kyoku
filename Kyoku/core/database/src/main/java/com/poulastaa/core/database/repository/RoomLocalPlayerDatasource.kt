package com.poulastaa.core.database.repository

import com.poulastaa.core.database.dao.CommonDao
import com.poulastaa.core.database.dao.LibraryDao
import com.poulastaa.core.database.dao.PlayerDao
import com.poulastaa.core.database.dao.ViewDao
import com.poulastaa.core.database.entity.PlayerInfoEntity
import com.poulastaa.core.database.mapper.toPlayerInfo
import com.poulastaa.core.database.mapper.toPlayerSong
import com.poulastaa.core.database.mapper.toPlayerSongEntity
import com.poulastaa.core.domain.PlayType
import com.poulastaa.core.domain.PlayerInfo
import com.poulastaa.core.domain.model.PlayerSong
import com.poulastaa.core.domain.repository.player.LocalPlayerDatasource
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RoomLocalPlayerDatasource @Inject constructor(
    private val commonDao: CommonDao,
    private val libraryDao: LibraryDao,
    private val viewDao: ViewDao,
    private val playerDao: PlayerDao
) : LocalPlayerDatasource {
    override suspend fun loadData(id: Long, type: PlayType) {
        coroutineScope {
            if (type != PlayType.SONG) {
                val clearSong = async { playerDao.clearPlayerSongs() }
                val clearInfo = async { playerDao.clearLoadInfo() }

                clearInfo.await()
                clearSong.await()
            }

            when (type) {
                PlayType.PLAYLIST -> {
                    val playlistDef = async { playerDao.getPlaylist(id) }
                    val songsEntity = async { playerDao.getPlaylistSongs(id) }


                    val playlist = playlistDef.await()
                    val songs = songsEntity.await()

                    val loadPlaylist = async {
                        playerDao.loadInfo(
                            entry = PlayerInfoEntity(
                                id = playlist.id,
                                type = playlist.name,
                                hasNext = songs.size > 1
                            )
                        )
                    }

                    val loadSongs = async {
                        songs.map {
                            async {
                                val isInFavourite = commonDao.isSongInFavourite(it.id)
                                it.toPlayerSongEntity(isInFavourite = isInFavourite != null)
                            }
                        }.awaitAll().let {
                            playerDao.loadPlayerSongs(it)
                        }
                    }

                    loadPlaylist.await()
                    loadSongs.await()
                }

                else -> Unit
            }
        }
    }

    override fun getInfo(): Flow<PlayerInfo> = playerDao.getInfo().map {
        if (it.isEmpty()) PlayerInfo()
        else it.map { entity -> entity.toPlayerInfo() }.first()
    }

    override fun getSongs(): Flow<List<PlayerSong>> =
        playerDao.getSong().map { list -> list.map { entity -> entity.toPlayerSong() } }
}