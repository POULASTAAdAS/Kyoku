package com.poulastaa.data.repository.login

import com.poulastaa.data.model.UserType
import com.poulastaa.data.model.auth.auth_response.*
import com.poulastaa.data.model.db_table.song.SongArtistRelationTable
import com.poulastaa.data.model.db_table.song.SongTable
import com.poulastaa.data.model.db_table.user_artist.EmailUserArtistRelationTable
import com.poulastaa.data.model.db_table.user_artist.GoogleUserArtistRelationTable
import com.poulastaa.data.model.db_table.user_artist.PasskeyUserArtistRelationTable
import com.poulastaa.domain.dao.song.Song
import com.poulastaa.domain.dao.song.SongArtistRelation
import com.poulastaa.domain.dao.user_artist.EmailUserArtistRelation
import com.poulastaa.domain.dao.user_artist.GoogleUserArtistRelation
import com.poulastaa.domain.dao.user_artist.PasskeyUserArtistRelation
import com.poulastaa.domain.repository.login.LogInResponseRepository
import com.poulastaa.plugins.dbQuery
import com.poulastaa.utils.constructCoverPhotoUrl
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.inList

class LogInResponseRepositoryImpl : LogInResponseRepository {
    override suspend fun getFevArtistMix(
        userId: Long,
        userType: UserType
    ): List<FevArtistsMixPreview> = when (userType) {
        UserType.GOOGLE_USER -> {
            dbQuery {
                GoogleUserArtistRelation.find {
                    GoogleUserArtistRelationTable.userId eq userId
                }.map {
                    it.artistId
                }.getListOfFevArtistMixPreview()
            }
        }

        UserType.EMAIL_USER -> {
            dbQuery {
                EmailUserArtistRelation.find {
                    EmailUserArtistRelationTable.userId eq userId
                }.map {
                    it.artistId
                }.getListOfFevArtistMixPreview()
            }
        }

        UserType.PASSKEY_USER -> {
            dbQuery {
                PasskeyUserArtistRelation.find {
                    PasskeyUserArtistRelationTable.userId eq userId
                }.map {
                    it.artistId
                }.getListOfFevArtistMixPreview()
            }
        }
    }

    override suspend fun getAlbumPrev(userId: Long, userType: UserType): ResponseAlbumPreview {
        return ResponseAlbumPreview()
    }

    override suspend fun getArtistPrev(userId: Long, userType: UserType): List<ResponseArtistsPreview> {
        return emptyList()
    }

    override suspend fun getDailyMixPrev(userId: Long, userType: UserType): DailyMixPreview {
        return DailyMixPreview()
    }

    override suspend fun getHistoryPrev(userId: Long, userType: UserType): List<SongPreview> {
        return emptyList()
    }

    override suspend fun getAlbums(userId: Long, userType: UserType): List<ResponseAlbum> {
        return emptyList()
    }

    override suspend fun getPlaylists(userId: Long, userType: UserType): List<ResponsePlaylist> {
        return emptyList()
    }

    override suspend fun getFavourites(userId: Long, userType: UserType): Favourites {
        return Favourites()
    }

    private suspend fun List<Int>.getListOfFevArtistMixPreview() = dbQuery {
        Song.find {
            SongTable.id inList SongArtistRelation.find {
                SongArtistRelationTable.artistId inList this@getListOfFevArtistMixPreview
            }.map {
                it.songId
            }
        }.orderBy(SongTable.points to SortOrder.DESC)
            .limit(4).map {
                FevArtistsMixPreview(
                    coverImage = it.coverImage.constructCoverPhotoUrl(),
                    artist = it.artist
                )
            }
    }
}