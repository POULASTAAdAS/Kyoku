package com.poulastaa.core.database.repository

import com.poulastaa.core.database.dao.CommonDao
import com.poulastaa.core.database.dao.LibraryDao
import com.poulastaa.core.database.dao.PlayerDao
import com.poulastaa.core.database.dao.ViewDao
import com.poulastaa.core.database.entity.PlayerInfoEntity
import com.poulastaa.core.database.entity.SongEntity
import com.poulastaa.core.database.mapper.toPlayerInfo
import com.poulastaa.core.database.mapper.toPlayerSong
import com.poulastaa.core.database.mapper.toPlayerSongEntity
import com.poulastaa.core.database.mapper.toPlaylist
import com.poulastaa.core.database.mapper.toPrevAlbum
import com.poulastaa.core.database.mapper.toSong
import com.poulastaa.core.domain.PlayerInfo
import com.poulastaa.core.domain.model.PlayerSong
import com.poulastaa.core.domain.model.Playlist
import com.poulastaa.core.domain.model.PrevAlbum
import com.poulastaa.core.domain.model.Song
import com.poulastaa.core.domain.repository.player.LocalPlayerDatasource
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RoomLocalPlayerDatasource @Inject constructor(
    private val commonDao: CommonDao,
    private val libraryDao: LibraryDao,
    private val viewDao: ViewDao,
    private val playerDao: PlayerDao
) : LocalPlayerDatasource {
    override suspend fun clearAll() {
        coroutineScope {
            listOf(
                async {
                    playerDao.clearPlayerSongs()
                },
                async {
                    playerDao.clearLoadInfo()
                }
            )
        }.awaitAll()
    }

    override suspend fun isPlaylistOrAlbumSaved(id: Long, isPlaylist: Boolean): Boolean =
        if (isPlaylist) libraryDao.getPlaylistOnId(id) != null
        else libraryDao.getAlbumOnId(id) != null

    override suspend fun getPlaylistSongs(id: Long): List<Song> =
        playerDao.getPlaylistSongs(id).map { it.toSong() }

    override suspend fun getPlaylist(id: Long): Playlist =
        libraryDao.getPlaylistOnId(id)!!.toPlaylist()

    override suspend fun getAlbumSongs(id: Long): List<Song> =
        playerDao.getAlbumSongs(id).map { it.toSong() }

    override suspend fun getAlbum(id: Long): PrevAlbum =
        commonDao.getAlbumById(id).toPrevAlbum()

    override suspend fun loadFev() {
        coroutineScope {
            val idList = commonDao.getFevSongIds()

            val songs = async {
                idList.let {
                    viewDao.getSongOnIdList(it)
                }.map {
                    it.toPlayerSongEntity(it.id)
                }.awaitAll().let {
                    playerDao.loadPlayerSongs(it)
                }
            }

            val info = async {
                playerDao.loadInfo(
                    entry = PlayerInfoEntity(
                        id = 1,
                        type = "Favourite",
                        hasNext = idList.size > 1,
                        isPlaying = false,
                        isShuffledEnabled = false,
                    )
                )
            }

            songs.await()
            info.await()
        }
    }

    override suspend fun loadOldMix() {
        coroutineScope {
            val idList = viewDao.getOldMixSongIds()

            val song = async {
                idList.let {
                    viewDao.getSongOnIdList(it)
                }.map {
                    it.toPlayerSongEntity(it.id)
                }.awaitAll().let {
                    playerDao.loadPlayerSongs(it)
                }
            }

            val info = async {
                playerDao.loadInfo(
                    entry = PlayerInfoEntity(
                        id = 1,
                        type = "Old Songs Mix",
                        hasNext = idList.size > 1,
                        isPlaying = false,
                        isShuffledEnabled = false,
                    )
                )
            }

            song.await()
            info.await()
        }
    }

    override suspend fun loadArtistMix() {
        coroutineScope {
            val idList = viewDao.getFevArtistMixSongIds()

            val song = async {
                idList.let {
                    viewDao.getSongOnIdList(it)
                }.map {
                    it.toPlayerSongEntity(it.id)
                }.awaitAll().let {
                    playerDao.loadPlayerSongs(it)
                }
            }

            val info = async {
                playerDao.loadInfo(
                    entry = PlayerInfoEntity(
                        id = 1,
                        type = "Favourite Artist Mix",
                        hasNext = idList.size > 1,
                        isPlaying = false,
                        isShuffledEnabled = false,
                    )
                )
            }

            song.await()
            info.await()
        }
    }

    override suspend fun loadPopularArtistMix() {
        coroutineScope {
            val idList = viewDao.getPopularSongMixSongIds()

            val song = async {
                idList.let {
                    viewDao.getSongOnIdList(it)
                }.map {
                    it.toPlayerSongEntity(it.id)
                }.awaitAll().let {
                    playerDao.loadPlayerSongs(it)
                }
            }

            val info = async {
                playerDao.loadInfo(
                    entry = PlayerInfoEntity(
                        id = 1,
                        type = "Popular Songs Mix",
                        hasNext = idList.size > 1,
                        isPlaying = false,
                        isShuffledEnabled = false,
                    )
                )
            }

            song.await()
            info.await()
        }
    }

    override suspend fun loadData(songs: List<Song>, id: Long, name: String) {
        coroutineScope {
            val loadPlaylist = async {
                playerDao.loadInfo(
                    entry = PlayerInfoEntity(
                        id = id,
                        type = name,
                        hasNext = songs.size > 1,
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
    }

    override fun getInfo(): Flow<PlayerInfo> =
        playerDao.getInfo().map {
            if (it.isEmpty()) PlayerInfo()
            else it.map { entity -> entity.toPlayerInfo() }.first()
        }

    override fun getSongs(): Flow<List<PlayerSong>> =
        playerDao.getSong().map { list ->
            list.map { entity -> entity.toPlayerSong() }
        }

    private suspend fun SongEntity.toPlayerSongEntity(id: Long) = coroutineScope {
        async {
            val isInFavourite = commonDao.isSongInFavourite(id)
            this@toPlayerSongEntity.toPlayerSongEntity(isInFavourite != null)
        }
    }
}