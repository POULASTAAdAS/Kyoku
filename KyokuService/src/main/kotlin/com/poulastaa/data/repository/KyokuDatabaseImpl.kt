package com.poulastaa.data.repository

import com.poulastaa.data.dao.AlbumDao
import com.poulastaa.data.dao.ArtistDao
import com.poulastaa.data.dao.PlaylistDao
import com.poulastaa.data.dao.SongDao
import com.poulastaa.data.mappers.toArtistResult
import com.poulastaa.data.mappers.toSongDto
import com.poulastaa.data.model.SongDto
import com.poulastaa.domain.model.ResultArtist
import com.poulastaa.domain.model.UserType
import com.poulastaa.domain.repository.DatabaseRepository
import com.poulastaa.domain.table.ArtistTable
import com.poulastaa.domain.table.GenreTable
import com.poulastaa.domain.table.PlaylistTable
import com.poulastaa.domain.table.SongTable
import com.poulastaa.domain.table.relation.SongArtistRelationTable
import com.poulastaa.domain.table.relation.UserPlaylistSongRelationTable
import com.poulastaa.plugins.query
import kotlinx.coroutines.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder
import org.jetbrains.exposed.sql.insertIgnore
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.update

class KyokuDatabaseImpl : DatabaseRepository {
    override fun updateSongPointByOne(list: List<Long>) {
        CoroutineScope(Dispatchers.IO).launch {
            query {
                SongTable.update({ SongTable.id inList list }) {
                    with(SqlExpressionBuilder) {
                        it[points] = points + 1
                    }
                }
            }
        }
    }

    override fun updateArtistPointByOne(list: List<Long>) {
        CoroutineScope(Dispatchers.IO).launch {
            query {
                ArtistTable.update({ ArtistTable.id inList list }) {
                    with(SqlExpressionBuilder) {
                        it[points] = points + 1
                    }
                }
            }
        }
    }

    override suspend fun getArtistOnSongId(songId: Long): List<ResultArtist> = query {
        val artistIdList = SongArtistRelationTable.select {
            SongArtistRelationTable.songId eq songId
        }.map {
            it[SongArtistRelationTable.artistId]
        }

        ArtistDao.find {
            ArtistTable.id inList artistIdList
        }.map {
            it.toArtistResult()
        }
    }

    override suspend fun getArtistOnSongIdList(
        list: List<Long>,
    ): List<Pair<Long, List<ResultArtist>>> = coroutineScope {
        val idMap = list.map {
            async {
                it to query {
                    SongArtistRelationTable.select {
                        SongArtistRelationTable.songId eq it
                    }.map {
                        it[SongArtistRelationTable.artistId]
                    }
                }
            }
        }.awaitAll()

        idMap.map { pair ->
            async {
                pair.first to pair.second.let { artistIdList ->
                    query {
                        ArtistDao.find {
                            ArtistTable.id inList artistIdList
                        }.map {
                            it.toArtistResult()
                        }
                    }
                }
            }
        }.awaitAll()
    }

    override fun updateGenrePointByOne(list: List<Int>) {
        CoroutineScope(Dispatchers.IO).launch {
            query {
                GenreTable.update({ GenreTable.id inList list }) {
                    with(SqlExpressionBuilder) {
                        it[points] = points + 1
                    }
                }
            }
        }
    }

    override suspend fun getSongOnId(id: Long): SongDto {
        val resultRow = query {
            SongArtistRelationTable.select {
                SongArtistRelationTable.songId eq id
            }
        }

        if (query { resultRow.empty() }) return SongDto()

        val artistName = query {
            resultRow.map {
                it[SongArtistRelationTable.artistId]
            }.let {
                ArtistDao.find {
                    ArtistTable.id inList it
                }.map {
                    it.name
                }
            }
        }.joinToString()

        return query {
            SongDao.find {
                SongTable.id eq id
            }.singleOrNull()?.toSongDto(artistName)
        } ?: SongDto()
    }

    override suspend fun createPlaylist(
        name: String,
        userId: Long,
        userType: UserType,
        songIdList: List<Long>,
    ): Long = coroutineScope {
        val playlist = async {
            query {
                PlaylistDao.new {
                    this.name = name
                }
            }
        }.await()

        songIdList.map { id ->
            async {
                query {
                    UserPlaylistSongRelationTable.insertIgnore {
                        it[this.playlistId] = playlist.id.value
                        it[this.songId] = id
                        it[this.userType] = userType.name
                        it[this.userId] = userId
                    }
                }
            }
        }.awaitAll()

        playlist.id.value
    }

    override suspend fun getPlaylistOnId(id: Long): PlaylistDao? = query {
        PlaylistDao.find {
            PlaylistTable.id eq id
        }.firstOrNull()
    }

    override suspend fun getAlbumOnId(albumId: Long): AlbumDao? = query {
        AlbumDao.find {
            ArtistTable.id eq albumId
        }.firstOrNull()
    }

    override suspend fun getArtistOnId(artistId: Long): ArtistDao? = query {
        ArtistDao.find {
            ArtistTable.id eq artistId
        }.firstOrNull()
    }
}