package com.poulastaa.core.database.repository.sync

import com.poulastaa.core.database.SQLDbManager.kyokuDbQuery
import com.poulastaa.core.database.SQLDbManager.userDbQuery
import com.poulastaa.core.database.entity.app.EntityPlaylist
import com.poulastaa.core.database.entity.app.EntitySong
import com.poulastaa.core.database.entity.app.RelationEntitySongAlbum
import com.poulastaa.core.database.entity.app.RelationEntitySongPlaylist
import com.poulastaa.core.database.entity.user.RelationEntityUserAlbum
import com.poulastaa.core.database.entity.user.RelationEntityUserArtist
import com.poulastaa.core.database.entity.user.RelationEntityUserFavouriteSong
import com.poulastaa.core.database.entity.user.RelationEntityUserPlaylist
import com.poulastaa.core.domain.model.*
import com.poulastaa.core.domain.repository.*
import com.poulastaa.core.domain.repository.sync.LocalSyncCacheDatasource
import com.poulastaa.core.domain.repository.sync.LocalSyncDatasource
import kotlinx.coroutines.*
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.inList
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.selectAll

internal class ExposedLocalSyncDatasource(
    private val core: LocalCoreDatasource,
    private val cache: LocalSyncCacheDatasource,
) : LocalSyncDatasource {
    override suspend fun getUsersByEmail(email: String, type: UserType): DtoDBUser? =
        core.getUserByEmail(email, type)

    override suspend fun getSavedAlbumIdList(userId: Long): List<AlbumId> = userDbQuery {
        RelationEntityUserAlbum.select(RelationEntityUserAlbum.albumId).where {
            RelationEntityUserAlbum.userId eq userId
        }.map { it[RelationEntityUserAlbum.albumId] }
    }

    override suspend fun getFullAlbumOnIdList(idList: List<AlbumId>): List<DtoFullAlbum> = coroutineScope {
        val all = cache.cacheAlbumById(idList).ifEmpty {
            core.getAlbumOnId(idList).also {
                cache.setAlbumById(it)
            }
        }

        val fullList = all + idList.filter { it !in all.map { item -> item.id } }.let {
            core.getAlbumOnId(idList).also {
                cache.setAlbumById(it)
            }
        }

        kyokuDbQuery {
            RelationEntitySongAlbum.selectAll().where {
                RelationEntitySongAlbum.albumId inList fullList.map { it.id }
            }.map {
                it[RelationEntitySongAlbum.albumId] to it[RelationEntitySongAlbum.songId]
            }
        }.groupBy { it.first }.map { it.key to it.value.map { item -> item.second } }.map { (albumId, songIdList) ->
            async {
                DtoFullAlbum(
                    album = fullList.first { it.id == albumId },
                    songs = getSongsOnId(songIdList)
                )
            }
        }.awaitAll()
    }

    override fun removeAlbum(idList: List<AlbumId>, userId: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            userDbQuery {
                RelationEntityUserAlbum.deleteWhere {
                    this.userId eq userId and (this.albumId inList idList)
                }
            }
        }
    }

    override suspend fun getSavedPlaylistIdList(userId: Long): List<PlaylistId> = userDbQuery {
        RelationEntityUserPlaylist.select(RelationEntityUserPlaylist.playlistId).where {
            RelationEntityUserPlaylist.userId eq userId
        }.map { it[RelationEntityUserPlaylist.playlistId] }
    }

    override suspend fun getFullPlaylistOnIdList(idList: List<PlaylistId>): List<DtoFullPlaylist> = coroutineScope {
        val playlist = cache.cachePlaylistOnId(idList).ifEmpty {
            core.getPlaylistOnId(idList).also {
                cache.setPlaylistById(it)
            }
        }

        val allPlaylist = playlist + idList.filter { it !in playlist.map { item -> item.id } }.let {
            core.getPlaylistOnId(idList).also {
                cache.setPlaylistById(it)
            }
        }

        kyokuDbQuery {
            EntityPlaylist
                .join(
                    otherTable = RelationEntitySongPlaylist,
                    joinType = JoinType.LEFT,
                    onColumn = EntityPlaylist.id,
                    otherColumn = RelationEntitySongPlaylist.playlistId
                ).join(
                    otherTable = EntitySong,
                    joinType = JoinType.LEFT,
                    onColumn = EntitySong.id,
                    otherColumn = RelationEntitySongPlaylist.songId
                ).select(
                    EntityPlaylist.id,
                    EntitySong.id
                ).where {
                    EntityPlaylist.id inList allPlaylist.map { it.id }
                }.map {
                    Pair<PlaylistId, SongId?>(
                        first = it[EntityPlaylist.id].value,
                        second = it.getOrNull(EntitySong.id)?.value
                    )
                }
        }.groupBy { it.first }.map {
            it.key to it.value.mapNotNull { map -> map.second }
        }.map { (playlistId, songIdList) ->
            async {
                DtoFullPlaylist(
                    playlist = allPlaylist.first { it.id == playlistId },
                    listOfSong = getSongsOnId(songIdList),
                )
            }
        }.awaitAll()
    }

    override fun removePlaylist(idList: List<PlaylistId>, userId: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            userDbQuery {
                RelationEntityUserPlaylist.deleteWhere {
                    this.userId eq userId and (this.playlistId inList idList)
                }
            }

            kyokuDbQuery {
                RelationEntitySongPlaylist.deleteWhere {
                    this.playlistId eq playlistId
                }
            }
        }
    }

    override suspend fun getSavedArtistIdList(userId: Long): List<ArtistId> = userDbQuery {
        RelationEntityUserArtist.select(RelationEntityUserArtist.artistId).where {
            RelationEntityUserArtist.userId eq userId
        }.map { it[RelationEntityUserArtist.artistId] }
    }

    override suspend fun getArtistOnIdList(idList: List<ArtistId>): List<DtoArtist> {
        val cache = cache.cacheArtistById(idList).ifEmpty {
            core.getArtistOnId(idList).also {
                cache.setArtistById(it)
            }
        }

        return cache + idList.filter { it !in cache.map { item -> item.id } }.let {
            core.getArtistOnId(idList).also {
                this.cache.setArtistById(it)
            }
        }
    }

    override fun removeArtist(idList: List<ArtistId>, userId: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            userDbQuery {
                RelationEntityUserArtist.deleteWhere {
                    this.userId eq userId and (this.artistId inList idList)
                }
            }
        }
    }

    override suspend fun getSavedFavouriteSongsIdList(userId: Long): List<SongId> = userDbQuery {
        RelationEntityUserFavouriteSong.select(RelationEntityUserFavouriteSong.songId).where {
            RelationEntityUserFavouriteSong.songId eq userId
        }.map { it[RelationEntityUserFavouriteSong.songId] }
    }

    override suspend fun getFavoriteSongs(idList: List<SongId>): List<DtoSong> = getSongsOnId(idList)

    override fun removeFavouriteSongs(idList: List<SongId>, userId: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            userDbQuery {
                RelationEntityUserFavouriteSong.deleteWhere {
                    this.userId eq userId and (this.songId inList idList)
                }
            }
        }
    }

    override suspend fun getPlaylistSongIdList(playlistId: PlaylistId): List<SongId> {
        suspend fun getPlaylistSongs() = kyokuDbQuery {
            RelationEntitySongPlaylist.select(RelationEntitySongPlaylist.songId).where {
                RelationEntitySongPlaylist.playlistId eq playlistId
            }.map { it[RelationEntitySongPlaylist.songId] as SongId }
        }.also {
            cache.setSongIdByPlaylistId(playlistId, it)
        }

        return cache.cacheSongIdByPlaylistId(playlistId).ifEmpty {
            getPlaylistSongs()
        }
    }

    override suspend fun getPlaylistSongs(list: List<SongId>): List<DtoSong> = getSongsOnId(list)

    override fun removePlaylistSongs(playlistId: PlaylistId, songIds: List<SongId>) {
        CoroutineScope(Dispatchers.IO).launch {
            userDbQuery {
                RelationEntitySongPlaylist.deleteWhere {
                    this.playlistId eq playlistId and (this.songId inList songIds)
                }
            }
        }
    }


    private suspend fun getSongsOnId(songIdList: List<Long>): List<DtoSong> {
        val songs = cache.cacheSongById(songIdList).ifEmpty {
            core.getSongOnId(songIdList).also {
                cache.setSongById(it)
            }
        }

        return songs + songIdList.filter { it !in songs.map { item -> item.id } }.let {
            core.getSongOnId(it).also { list ->
                cache.setSongById(list)
            }
        }
    }
}