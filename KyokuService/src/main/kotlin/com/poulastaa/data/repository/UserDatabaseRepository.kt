package com.poulastaa.data.repository

import com.poulastaa.data.dao.ArtistDao
import com.poulastaa.data.dao.PlaylistDao
import com.poulastaa.data.dao.SongDao
import com.poulastaa.data.dao.user.EmailAuthUserDao
import com.poulastaa.data.dao.user.GoogleAuthUserDao
import com.poulastaa.data.mappers.toSongDto
import com.poulastaa.data.mappers.toUserResult
import com.poulastaa.data.model.ArtistDto
import com.poulastaa.data.model.PlaylistDto
import com.poulastaa.data.model.home.HomeDto
import com.poulastaa.domain.model.PlaylistSongPayload
import com.poulastaa.domain.model.ReqUserPayload
import com.poulastaa.domain.model.UserResult
import com.poulastaa.domain.model.UserType
import com.poulastaa.domain.repository.UserRepository
import com.poulastaa.domain.table.ArtistTable
import com.poulastaa.domain.table.PlaylistTable
import com.poulastaa.domain.table.SongTable
import com.poulastaa.domain.table.relation.SongArtistRelationTable
import com.poulastaa.domain.table.relation.UserArtistRelationTable
import com.poulastaa.domain.table.relation.UserGenreRelationTable
import com.poulastaa.domain.table.relation.UserPlaylistSongRelationTable
import com.poulastaa.domain.table.user.EmailAuthUserTable
import com.poulastaa.domain.table.user.GoogleAuthUserTable
import com.poulastaa.plugins.query
import kotlinx.coroutines.*
import org.jetbrains.exposed.sql.*

class UserDatabaseRepository : UserRepository {
    override suspend fun getUserOnPayload(payload: ReqUserPayload): UserResult? {
        return when (payload.userType) {
            UserType.GOOGLE_USER -> {
                query {
                    GoogleAuthUserDao.find {
                        GoogleAuthUserTable.id eq payload.id.toLong()
                    }.singleOrNull()?.toUserResult()
                }
            }

            UserType.EMAIL_USER -> {
                query {
                    EmailAuthUserDao.find {
                        EmailAuthUserTable.email eq payload.id
                    }.singleOrNull()?.toUserResult()
                }
            }
        }
    }

    override fun createUserPlaylist(
        userId: Long,
        userType: UserType,
        playlistId: Long,
        songIdList: List<Long>,
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            query {
                UserPlaylistSongRelationTable.batchInsert(songIdList, ignore = true) { songId ->
                    this[UserPlaylistSongRelationTable.userId] = userId
                    this[UserPlaylistSongRelationTable.playlistId] = playlistId
                    this[UserPlaylistSongRelationTable.songId] = songId
                    this[UserPlaylistSongRelationTable.userType] = userType.name
                }
            }
        }
    }

    override suspend fun updateBDate(
        userId: Long,
        bDate: Long,
        userType: UserType,
    ): Boolean {
        when (userType) {
            UserType.GOOGLE_USER -> {
                query {
                    GoogleAuthUserDao.find {
                        GoogleAuthUserTable.id eq userId
                    }.single().bDate = bDate
                }
            }

            UserType.EMAIL_USER -> {
                query {
                    EmailAuthUserDao.find {
                        EmailAuthUserTable.id eq userId
                    }.single().bDate = bDate
                }
            }
        }

        return true
    }

    override suspend fun storeGenre(
        userId: Long,
        userType: UserType,
        idList: List<Int>,
    ) {
        coroutineScope {
            query {
                UserGenreRelationTable.batchInsert(idList, ignore = true) { genreId ->
                    this[UserGenreRelationTable.userId] = userId
                    this[UserGenreRelationTable.userType] = userType.name
                    this[UserGenreRelationTable.genreId] = genreId
                }
            }
        }
    }

    override suspend fun storeArtist(userId: Long, userType: UserType, idList: List<Long>) {
        coroutineScope {
            query {
                UserArtistRelationTable.batchInsert(idList, ignore = true) { artistId ->
                    this[UserArtistRelationTable.userId] = userId
                    this[UserArtistRelationTable.userType] = userType.name
                    this[UserArtistRelationTable.artistId] = artistId
                }
            }
        }
    }

    override suspend fun getUserData(
        userType: UserType,
        userId: Long,
    ): HomeDto = coroutineScope {
        val playlistDef = async {
            query {
                UserPlaylistSongRelationTable.select {
                    (UserPlaylistSongRelationTable.userId eq userId) and (UserPlaylistSongRelationTable.userType eq userType.name)
                }.map {
                    PlaylistSongPayload(
                        playlistId = it[UserPlaylistSongRelationTable.playlistId],
                        songId = it[UserPlaylistSongRelationTable.songId],
                    )
                }
            }.groupBy { it.playlistId }.map {
                async {
                    query {
                        PlaylistDao.find {
                            PlaylistTable.id eq it.key
                        }.first()
                    } to query {
                        SongDao.find {
                            SongTable.id inList it.value.map { it.songId }
                        }.toList()
                    }
                }
            }.awaitAll().map {
                PlaylistDto(
                    id = it.first.id.value,
                    name = it.first.name,
                    listOfSong = it.second.map { songDao ->
                        async {
                            query {
                                ArtistTable.join(
                                    otherTable = SongArtistRelationTable,
                                    joinType = JoinType.INNER,
                                    additionalConstraint = {
                                        SongArtistRelationTable.artistId eq ArtistTable.id as Column<*>
                                    }
                                ).slice(ArtistTable.name)
                                    .select {
                                        SongArtistRelationTable.songId eq SongTable.id as Column<*>
                                    }.map { resultRow ->
                                        resultRow[ArtistTable.name]
                                    }
                            } to songDao
                        }
                    }.awaitAll().map { pair ->
                        pair.second.toSongDto(artist = pair.first.joinToString())
                    }
                )
            }
        }

        val albumDef = async {
            query {
            }
        }

        val artistDef = async {
            query {
                UserArtistRelationTable.select {
                    UserArtistRelationTable.userId eq userId and (UserArtistRelationTable.userType eq userType.name)
                }.map {
                    it[UserArtistRelationTable.artistId]
                }.let {
                    query {
                        ArtistDao.find {
                            ArtistTable.id inList it
                        }
                    }
                }.map {
                    ArtistDto(
                        id = it.id.value,
                        name = it.name,
                        coverImage = it.constructProfilePic()
                    )
                }
            }
        }

        HomeDto()
    }
}