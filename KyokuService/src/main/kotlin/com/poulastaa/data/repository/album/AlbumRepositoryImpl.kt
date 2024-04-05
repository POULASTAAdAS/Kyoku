package com.poulastaa.data.repository.album

import com.poulastaa.data.model.db_table.AlbumTable
import com.poulastaa.data.model.db_table.SongAlbumArtistRelationTable
import com.poulastaa.data.model.db_table.SongTable
import com.poulastaa.data.model.db_table.user_album.EmailUserAlbumRelation
import com.poulastaa.data.model.db_table.user_album.GoogleUserAlbumRelation
import com.poulastaa.data.model.db_table.user_album.PasskeyUserAlbumRelation
import com.poulastaa.data.model.db_table.user_pinned_album.EmailUserPinnedAlbumTable
import com.poulastaa.data.model.db_table.user_pinned_album.GoogleUserPinnedAlbumTable
import com.poulastaa.data.model.db_table.user_pinned_album.PasskeyUserPinnedAlbumTable
import com.poulastaa.data.model.home.AlbumPreview
import com.poulastaa.data.model.home.ResponseAlbumPreview
import com.poulastaa.data.model.home.SongPreview
import com.poulastaa.data.model.item.ItemOperation
import com.poulastaa.data.model.utils.AlbumResult
import com.poulastaa.data.model.utils.UserType
import com.poulastaa.domain.dao.Album
import com.poulastaa.domain.repository.album.AlbumRepository
import com.poulastaa.plugins.dbQuery
import com.poulastaa.utils.constructCoverPhotoUrl
import com.poulastaa.utils.toPreviewSong
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.util.Random

class AlbumRepositoryImpl : AlbumRepository {
    override suspend fun getResponseAlbumPreviewForNewUser(artistIdList: List<Int>) =
        ResponseAlbumPreview(
            listOfPreviewAlbum = dbQuery {
                SongTable
                    .join(
                        otherTable = SongAlbumArtistRelationTable,
                        joinType = JoinType.INNER,
                        additionalConstraint = {
                            SongTable.id eq SongAlbumArtistRelationTable.songId as Column<*>
                        }
                    ).join(
                        otherTable = AlbumTable,
                        joinType = JoinType.INNER,
                        additionalConstraint = {
                            SongAlbumArtistRelationTable.albumId as Column<*> eq AlbumTable.id
                        }
                    ).slice(
                        AlbumTable.id,
                        SongTable.id,
                        SongTable.title,
                        SongTable.coverImage,
                        SongTable.artist,
                        SongTable.album
                    ).select {
                        AlbumTable.id inSubQuery (
                                SongAlbumArtistRelationTable
                                    .slice(SongAlbumArtistRelationTable.albumId)
                                    .select {
                                        SongAlbumArtistRelationTable.artistId inList artistIdList
                                    }
                                )
                    }
                    .orderBy(AlbumTable.points, SortOrder.DESC)
                    .limit(30)
                    .asSequence()
                    .map {
                        AlbumResult(
                            albumId = it[AlbumTable.id].value,
                            name = it[SongTable.album],
                            albumPoints = 0,
                            songId = it[SongTable.id].value,
                            title = it[SongTable.title],
                            artist = it[SongTable.artist],
                            cover = it[SongTable.coverImage].constructCoverPhotoUrl(),
                            points = 0,
                            year = "0"
                        )
                    }
                    .groupBy { it.albumId }
                    .map {
                        AlbumPreview(
                            id = it.key,
                            name = it.value[0].name,
                            listOfSongs = it.value.toPreviewSong().take(4)
                        )
                    }
                    .take(5)
                    .toList()
            }
        )

    override suspend fun getResponseAlbumPreviewForDailyRefresh(
        userType: UserType,
        userId: Long
    ): ResponseAlbumPreview {
        // get users fev albums max 2
        val userFevAlbumIdList = getFevAlbumIdList(userType, userId)

        // get most popular albums remove duplicate limit 7
        // make a single list take 5
        val albumIdList = (userFevAlbumIdList + userFevAlbumIdList.getMostPopularAlbumsIdList()).take(5)


        return ResponseAlbumPreview(
            listOfPreviewAlbum = albumIdList.getAlbumOnAlbumIdList()
        )
    }


    override suspend fun handleAlbum(
        userId: Long,
        userType: UserType,
        albumId: Long,
        operation: ItemOperation
    ): Boolean = withContext(Dispatchers.IO) {
        when (operation) {
            ItemOperation.ADD -> addAlbum(albumId = albumId, userId = userId, userType = userType)

            ItemOperation.DELETE -> {
                async { deleteUserAlbum(albumId = albumId, userId = userId, userType = userType) }.await()
                async { deletePinnedAlbum(albumId = albumId, userId = userId, userType = userType) }.await()
            }

            ItemOperation.ERR -> false
        }
    }

    private suspend fun getFevAlbumIdList(userType: UserType, userId: Long) = dbQuery {
        when (userType) {
            UserType.GOOGLE_USER -> {
                GoogleUserAlbumRelation.slice(GoogleUserAlbumRelation.albumId)
                    .select {
                        GoogleUserAlbumRelation.userId eq userId
                    }.orderBy(GoogleUserAlbumRelation.points, SortOrder.DESC)
                    .limit(2)
                    .map {
                        it[GoogleUserAlbumRelation.albumId]
                    }
            }

            UserType.EMAIL_USER -> {
                EmailUserAlbumRelation.slice(EmailUserAlbumRelation.albumId)
                    .select {
                        EmailUserAlbumRelation.userId eq userId
                    }.orderBy(EmailUserAlbumRelation.points, SortOrder.DESC)
                    .limit(2)
                    .map {
                        it[EmailUserAlbumRelation.albumId]
                    }
            }

            UserType.PASSKEY_USER -> {
                PasskeyUserAlbumRelation.slice(PasskeyUserAlbumRelation.albumId)
                    .select {
                        PasskeyUserAlbumRelation.userId eq userId
                    }.orderBy(PasskeyUserAlbumRelation.points, SortOrder.DESC)
                    .limit(2)
                    .map {
                        it[PasskeyUserAlbumRelation.albumId]
                    }
            }
        }
    }

    private suspend fun List<Long>.getMostPopularAlbumsIdList() = dbQuery {
        Album.all()
            .orderBy(AlbumTable.points to SortOrder.DESC)
            .filterNot { album ->
                this.any {
                    album.id.value == it
                }
            }.map { it.id.value }
            .take(7)
            .shuffled(Random())
    }

    private suspend fun List<Long>.getAlbumOnAlbumIdList() = dbQuery {
        SongTable
            .join(
                otherTable = SongAlbumArtistRelationTable,
                joinType = JoinType.INNER,
                additionalConstraint = {
                    SongTable.id eq SongAlbumArtistRelationTable.songId as Column<*>
                }
            ).join(
                otherTable = AlbumTable,
                joinType = JoinType.INNER,
                additionalConstraint = {
                    SongAlbumArtistRelationTable.albumId as Column<*> eq AlbumTable.id
                }
            ).slice(
                SongTable.id,
                SongTable.title,
                SongTable.coverImage,
                SongTable.artist,
                SongTable.album
            ).select {
                AlbumTable.id inList this@getAlbumOnAlbumIdList
            }.orderBy(AlbumTable.points, SortOrder.ASC)
            .map {
                SongPreview(
                    id = it[SongTable.id].value.toString(),
                    title = it[SongTable.title],
                    coverImage = it[SongTable.coverImage].constructCoverPhotoUrl(),
                    artist = it[SongTable.artist],
                    album = it[SongTable.album]
                )
            }.groupBy {
                it.album
            }.map {
                AlbumPreview(
                    name = it.key,
                    listOfSongs = it.value.take(4)
                )
            }
    }


    private suspend fun addAlbum(
        albumId: Long,
        userId: Long,
        userType: UserType
    ) = dbQuery {
        when (userType) {
            UserType.GOOGLE_USER -> {
                GoogleUserAlbumRelation.insertIgnore {
                    it[GoogleUserAlbumRelation.albumId] = albumId
                    it[GoogleUserAlbumRelation.userId] = userId
                }
            }

            UserType.EMAIL_USER -> {
                EmailUserAlbumRelation.insertIgnore {
                    it[EmailUserAlbumRelation.albumId] = albumId
                    it[EmailUserAlbumRelation.userId] = userId
                }
            }

            UserType.PASSKEY_USER -> {
                PasskeyUserAlbumRelation.insertIgnore {
                    it[PasskeyUserAlbumRelation.albumId] = albumId
                    it[PasskeyUserAlbumRelation.userId] = userId
                }
            }
        }
    }.let { true }

    private suspend fun deleteUserAlbum(
        albumId: Long,
        userId: Long,
        userType: UserType
    ) = dbQuery {
        when (userType) {
            UserType.GOOGLE_USER -> {
                GoogleUserAlbumRelation.deleteWhere {
                    GoogleUserAlbumRelation.albumId eq albumId and (GoogleUserAlbumRelation.userId eq userId)
                }
            }

            UserType.EMAIL_USER -> {
                EmailUserAlbumRelation.deleteWhere {
                    EmailUserAlbumRelation.albumId eq albumId and (EmailUserAlbumRelation.userId eq userId)
                }
            }

            UserType.PASSKEY_USER -> {
                PasskeyUserAlbumRelation.deleteWhere {
                    PasskeyUserAlbumRelation.albumId eq albumId and (PasskeyUserAlbumRelation.userId eq userId)
                }
            }
        }
    }.let { true }

    private suspend fun deletePinnedAlbum(
        albumId: Long,
        userId: Long,
        userType: UserType
    ) = dbQuery {
        when (userType) {
            UserType.GOOGLE_USER -> {
                GoogleUserPinnedAlbumTable.deleteWhere {
                    GoogleUserPinnedAlbumTable.albumId eq albumId and (GoogleUserPinnedAlbumTable.userId eq userId)
                }
            }

            UserType.EMAIL_USER -> {
                EmailUserPinnedAlbumTable.deleteWhere {
                    EmailUserPinnedAlbumTable.albumId eq albumId and (EmailUserPinnedAlbumTable.userId eq userId)
                }
            }

            UserType.PASSKEY_USER -> {
                PasskeyUserPinnedAlbumTable.deleteWhere {
                    PasskeyUserPinnedAlbumTable.albumId eq albumId and (PasskeyUserPinnedAlbumTable.userId eq userId)
                }
            }
        }
    }.let { true }
}
