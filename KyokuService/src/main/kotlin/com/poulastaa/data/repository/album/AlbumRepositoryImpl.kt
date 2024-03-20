package com.poulastaa.data.repository.album

import com.poulastaa.data.model.db_table.AlbumTable
import com.poulastaa.data.model.db_table.SongAlbumArtistRelationTable
import com.poulastaa.data.model.db_table.SongTable
import com.poulastaa.data.model.db_table.user_album.EmailUserAlbumRelation
import com.poulastaa.data.model.db_table.user_album.GoogleUserAlbumRelation
import com.poulastaa.data.model.db_table.user_album.PasskeyUserAlbumRelation
import com.poulastaa.data.model.home.AlbumPreview
import com.poulastaa.data.model.home.ResponseAlbumPreview
import com.poulastaa.data.model.home.SongPreview
import com.poulastaa.data.model.utils.UserType
import com.poulastaa.domain.dao.Album
import com.poulastaa.domain.repository.album.AlbumRepository
import com.poulastaa.plugins.dbQuery
import com.poulastaa.utils.constructCoverPhotoUrl
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.select
import java.util.*

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
}
