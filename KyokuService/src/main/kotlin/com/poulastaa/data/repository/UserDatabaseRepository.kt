package com.poulastaa.data.repository

import com.poulastaa.data.dao.user.EmailAuthUserDao
import com.poulastaa.data.dao.user.GoogleAuthUserDao
import com.poulastaa.data.mappers.toUserResult
import com.poulastaa.domain.model.ReqUserPayload
import com.poulastaa.domain.model.UserResult
import com.poulastaa.domain.model.UserType
import com.poulastaa.domain.repository.UserRepository
import com.poulastaa.domain.table.relation.UserPlaylistSongRelationTable
import com.poulastaa.domain.table.user.EmailAuthUserTable
import com.poulastaa.domain.table.user.GoogleAuthUserTable
import com.poulastaa.plugins.query
import kotlinx.coroutines.*
import org.jetbrains.exposed.sql.insertIgnore

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

    override suspend fun createUserPlaylist(
        userId: Long,
        userType: UserType,
        playlistId: Long,
        songIdList: List<Long>,
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            songIdList.map { songId ->
                async {
                    query {
                        UserPlaylistSongRelationTable.insertIgnore {
                            it[this.userId] = userId
                            it[this.playlistId] = playlistId
                            it[this.songId] = songId
                            it[this.userType] = userType.name
                        }
                    }
                }
            }.awaitAll()
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
}