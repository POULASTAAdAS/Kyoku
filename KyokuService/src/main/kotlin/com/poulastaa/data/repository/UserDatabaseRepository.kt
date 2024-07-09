package com.poulastaa.data.repository

import com.poulastaa.data.dao.user.EmailAuthUserDao
import com.poulastaa.data.dao.user.GoogleAuthUserDao
import com.poulastaa.data.mappers.toUserResult
import com.poulastaa.domain.model.ReqUserPayload
import com.poulastaa.domain.model.UserResult
import com.poulastaa.domain.model.UserType
import com.poulastaa.domain.repository.UserRepository
import com.poulastaa.domain.table.relation.UserArtistRelationTable
import com.poulastaa.domain.table.relation.UserGenreRelationTable
import com.poulastaa.domain.table.relation.UserPlaylistSongRelationTable
import com.poulastaa.domain.table.user.EmailAuthUserTable
import com.poulastaa.domain.table.user.GoogleAuthUserTable
import com.poulastaa.plugins.query
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.exposed.sql.batchInsert

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
}