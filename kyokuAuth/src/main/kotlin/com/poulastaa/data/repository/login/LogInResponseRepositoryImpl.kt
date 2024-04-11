package com.poulastaa.data.repository.login

import com.poulastaa.data.model.UserType
import com.poulastaa.data.model.auth.auth_response.*
import com.poulastaa.data.model.db_table.AlbumTable
import com.poulastaa.data.model.db_table.ArtistTable
import com.poulastaa.data.model.db_table.DbResponseArtistPreview
import com.poulastaa.data.model.db_table.DbResponseArtistPreview.Companion.toListOfSongPreview
import com.poulastaa.data.model.db_table.PlaylistTable
import com.poulastaa.data.model.db_table.song.SongAlbumArtistRelationTable
import com.poulastaa.data.model.db_table.song.SongArtistRelationTable
import com.poulastaa.data.model.db_table.song.SongTable
import com.poulastaa.data.model.db_table.user_album.EmailUserAlbumRelation
import com.poulastaa.data.model.db_table.user_album.GoogleUserAlbumRelation
import com.poulastaa.data.model.db_table.user_album.PasskeyUserAlbumRelation
import com.poulastaa.data.model.db_table.user_artist.EmailUserArtistRelationTable
import com.poulastaa.data.model.db_table.user_artist.GoogleUserArtistRelationTable
import com.poulastaa.data.model.db_table.user_artist.PasskeyUserArtistRelationTable
import com.poulastaa.data.model.db_table.user_fev.EmailUserFavouriteTable
import com.poulastaa.data.model.db_table.user_fev.GoogleUserFavouriteTable
import com.poulastaa.data.model.db_table.user_fev.PasskeyUserFavouriteTable
import com.poulastaa.data.model.db_table.user_listen_history.EmailUserListenHistoryTable
import com.poulastaa.data.model.db_table.user_listen_history.GoogleUserListenHistoryTable
import com.poulastaa.data.model.db_table.user_listen_history.PasskeyUserListenHistoryTable
import com.poulastaa.data.model.db_table.user_pinned_album.EmailUserPinnedAlbumTable
import com.poulastaa.data.model.db_table.user_pinned_album.GoogleUserPinnedAlbumTable
import com.poulastaa.data.model.db_table.user_pinned_album.PasskeyUserPinnedAlbumTable
import com.poulastaa.data.model.db_table.user_pinned_artist.EmailUserPinnedArtistTable
import com.poulastaa.data.model.db_table.user_pinned_artist.GoogleUserPinnedArtistTable
import com.poulastaa.data.model.db_table.user_pinned_artist.PasskeyUserPinnedArtistTable
import com.poulastaa.data.model.db_table.user_pinned_playlist.EmailUserPinnedPlaylistTable
import com.poulastaa.data.model.db_table.user_pinned_playlist.GoogleUserPinnedPlaylistTable
import com.poulastaa.data.model.db_table.user_pinned_playlist.PasskeyUserPinnedPlaylistTable
import com.poulastaa.data.model.db_table.user_playlist.EmailUserPlaylistTable
import com.poulastaa.data.model.db_table.user_playlist.GoogleUserPlaylistTable
import com.poulastaa.data.model.db_table.user_playlist.PasskeyUserPlaylistTable
import com.poulastaa.data.model.utils.AlbumResult
import com.poulastaa.domain.dao.Album
import com.poulastaa.domain.dao.song.Song
import com.poulastaa.domain.dao.song.SongArtistRelation
import com.poulastaa.domain.dao.user_artist.EmailUserArtistRelation
import com.poulastaa.domain.dao.user_artist.GoogleUserArtistRelation
import com.poulastaa.domain.dao.user_artist.PasskeyUserArtistRelation
import com.poulastaa.domain.repository.login.LogInResponseRepository
import com.poulastaa.plugins.dbQuery
import com.poulastaa.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.*
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.Random
import java.util.concurrent.TimeUnit

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
        // get users fev albums max 2
        val userFevAlbumIdList = getFevAlbumIdList(userType, userId)

        // get most popular albums remove duplicate limit 7
        // make a single list take 5
        val albumIdList = (userFevAlbumIdList + userFevAlbumIdList.getMostPopularAlbumsIdList()).take(5)

        return ResponseAlbumPreview(
            listOfPreviewAlbum = albumIdList.getAlbumOnAlbumIdList()
        )
    }

    override suspend fun getArtistPrev(userId: Long, userType: UserType): List<ResponseArtistsPreview> =
        withContext(Dispatchers.IO) {
            async { getFevArtistIdList(userType, userId) }.await()
                .getResponseArtistPreviewOnArtistIdList()
        }

    // getDailyMixPrev
    override suspend fun getDailyMixPrev(userId: Long, userType: UserType): DailyMixPreview {
        val historySongIdList = getHistorySongIdList(userType, userId) // get artistId from history

        println(historySongIdList)

        val songsByTheArtistUnSorted = getPreviewSongsByTheArtists(historySongIdList)

        return DailyMixPreview(
            listOfSongs = songsByTheArtistUnSorted.flatMap {
                it.value.take(10)
            }.distinctBy {
                it.id
            }.shuffled(Random()).take(4)
        )
    }

    // last played
    override suspend fun getHistoryPrev(userId: Long, userType: UserType): List<SongPreview> = dbQuery {
        when (userType) {
            UserType.GOOGLE_USER -> {
                GoogleUserListenHistoryTable
                    .slice(
                        GoogleUserListenHistoryTable.songId
                    ).select {
                        GoogleUserListenHistoryTable.userId eq userId
                    }
                    .orderBy(GoogleUserListenHistoryTable.date, SortOrder.DESC)
                    .limit(7)
                    .map {
                        it[GoogleUserListenHistoryTable.songId]
                    }
            }

            UserType.EMAIL_USER -> {
                EmailUserListenHistoryTable
                    .slice(
                        EmailUserListenHistoryTable.songId
                    ).select {
                        EmailUserListenHistoryTable.userId eq userId
                    }
                    .orderBy(EmailUserListenHistoryTable.date, SortOrder.DESC)
                    .limit(7)
                    .map {
                        it[EmailUserListenHistoryTable.songId]
                    }
            }

            UserType.PASSKEY_USER -> {
                PasskeyUserListenHistoryTable
                    .slice(
                        PasskeyUserListenHistoryTable.songId
                    ).select {
                        PasskeyUserListenHistoryTable.userId eq userId
                    }
                    .orderBy(PasskeyUserListenHistoryTable.date, SortOrder.DESC)
                    .limit(7)
                    .map {
                        it[PasskeyUserListenHistoryTable.songId]
                    }
            }
        }.let {
            Song.find {
                SongTable.id inList it
            }.map {
                SongPreview(
                    id = it.id.value.toString(),
                    title = it.title,
                    coverImage = it.coverImage.constructCoverPhotoUrl(),
                    artist = it.artist,
                    album = it.album
                )
            }
        }
    }

    override suspend fun getPinnedData(
        userId: Long,
        userType: UserType
    ): List<Pinned> = withContext(Dispatchers.IO) {
        val artist = async { getPinnedArtist(userType, userId) }
        val album = async { getPinnedAlbum(userType, userId) }
        val playlist = async { getPinnedPlaylist(userId, userType) }

        artist.await() + album.await() + playlist.await()
    }

    override suspend fun getAlbums(userId: Long, userType: UserType): List<ResponseAlbum> = dbQuery {
        val albumIdList = when (userType) {
            UserType.GOOGLE_USER -> {
                GoogleUserAlbumRelation
                    .slice(
                        GoogleUserAlbumRelation.albumId
                    ).select {
                        GoogleUserAlbumRelation.userId eq userId
                    }.orderBy(GoogleUserAlbumRelation.points, SortOrder.DESC)
                    .map {
                        it[GoogleUserAlbumRelation.albumId]
                    }
            }

            UserType.EMAIL_USER -> {
                EmailUserAlbumRelation
                    .slice(
                        EmailUserAlbumRelation.albumId
                    ).select {
                        EmailUserAlbumRelation.userId eq userId
                    }.orderBy(EmailUserAlbumRelation.points, SortOrder.DESC)
                    .map {
                        it[EmailUserAlbumRelation.albumId]
                    }
            }

            UserType.PASSKEY_USER -> {
                PasskeyUserAlbumRelation
                    .slice(
                        PasskeyUserAlbumRelation.albumId
                    ).select {
                        PasskeyUserAlbumRelation.userId eq userId
                    }.orderBy(PasskeyUserAlbumRelation.points, SortOrder.DESC)
                    .map {
                        it[PasskeyUserAlbumRelation.albumId]
                    }
            }
        }

        SongTable
            .join(
                otherTable = SongAlbumArtistRelationTable,
                joinType = JoinType.INNER,
                additionalConstraint = {
                    SongAlbumArtistRelationTable.songId as Column<*> eq SongTable.id
                }
            )
            .join(
                otherTable = when (userType) {
                    UserType.GOOGLE_USER -> GoogleUserAlbumRelation
                    UserType.EMAIL_USER -> EmailUserAlbumRelation
                    UserType.PASSKEY_USER -> PasskeyUserAlbumRelation
                },
                joinType = JoinType.INNER,
                additionalConstraint = {
                    SongAlbumArtistRelationTable.albumId eq when (userType) {
                        UserType.GOOGLE_USER -> GoogleUserAlbumRelation.albumId
                        UserType.EMAIL_USER -> EmailUserAlbumRelation.albumId
                        UserType.PASSKEY_USER -> PasskeyUserAlbumRelation.albumId
                    }
                }
            ).slice(
                SongTable.id,
                SongTable.title,
                SongTable.album,
                SongTable.artist,
                SongTable.coverImage,
                SongTable.masterPlaylistPath,
                SongTable.totalTime,
                SongTable.genre,
                SongTable.publisher,
                SongTable.composer,
                SongTable.album_artist,
                SongTable.description,
                SongTable.track,
                SongTable.date,
                when (userType) {
                    UserType.GOOGLE_USER -> GoogleUserAlbumRelation.albumId
                    UserType.EMAIL_USER -> EmailUserAlbumRelation.albumId
                    UserType.PASSKEY_USER -> PasskeyUserAlbumRelation.albumId
                }
            ).select {
                when (userType) {
                    UserType.GOOGLE_USER -> GoogleUserAlbumRelation.albumId inList albumIdList
                    UserType.EMAIL_USER -> EmailUserAlbumRelation.albumId inList albumIdList
                    UserType.PASSKEY_USER -> PasskeyUserAlbumRelation.albumId inList albumIdList
                }
            }.map {
                it.toAlbumResponse(userType)
            }.groupBy {
                it.name
            }.map {
                ResponseAlbum(
                    id = it.value[0].id,
                    name = it.key,
                    listOfSongs = it.value.map { response ->
                        response.song
                    }
                )
            }
    }

    override suspend fun getPlaylists(userId: Long, userType: UserType): List<ResponsePlaylist> = dbQuery {
        SongTable
            .join(
                otherTable = when (userType) {
                    UserType.GOOGLE_USER -> GoogleUserPlaylistTable
                    UserType.EMAIL_USER -> EmailUserPlaylistTable
                    UserType.PASSKEY_USER -> PasskeyUserPlaylistTable
                },
                joinType = JoinType.INNER,
                additionalConstraint = {
                    SongTable.id eq when (userType) {
                        UserType.GOOGLE_USER -> GoogleUserPlaylistTable.songId
                        UserType.EMAIL_USER -> EmailUserPlaylistTable.songId
                        UserType.PASSKEY_USER -> PasskeyUserPlaylistTable.songId
                    } as Column<*>
                }
            ).join(
                otherTable = PlaylistTable,
                joinType = JoinType.INNER,
                additionalConstraint = {
                    when (userType) {
                        UserType.GOOGLE_USER -> GoogleUserPlaylistTable.playlistId
                        UserType.EMAIL_USER -> EmailUserPlaylistTable.playlistId
                        UserType.PASSKEY_USER -> PasskeyUserPlaylistTable.playlistId
                    } as Column<*> eq PlaylistTable.id
                }
            )
            .slice(
                SongTable.id,
                SongTable.title,
                SongTable.album,
                SongTable.artist,
                SongTable.coverImage,
                SongTable.masterPlaylistPath,
                SongTable.totalTime,
                SongTable.genre,
                SongTable.publisher,
                SongTable.composer,
                SongTable.album_artist,
                SongTable.description,
                SongTable.track,
                SongTable.date,

                PlaylistTable.id,
                PlaylistTable.name
            )
            .select {
                when (userType) {
                    UserType.GOOGLE_USER -> GoogleUserPlaylistTable.userId
                    UserType.EMAIL_USER -> EmailUserPlaylistTable.userId
                    UserType.PASSKEY_USER -> PasskeyUserPlaylistTable.userId
                } eq userId
            }.orderBy(PlaylistTable.points, SortOrder.DESC)
            .map {
                it.toPlaylistResult()
            }.groupBy {
                it.playlistId
            }.map {
                ResponsePlaylist(
                    id = it.key,
                    name = it.value[0].playlistName,
                    listOfSongs = it.value.map { song ->
                        song.toResponseSong()
                    }
                )
            }
    }

    override suspend fun getFavourites(userId: Long, userType: UserType): Favourites = Favourites(
        listOfSongs = dbQuery {
            SongTable
                .join(
                    otherTable = when (userType) {
                        UserType.GOOGLE_USER -> GoogleUserFavouriteTable
                        UserType.EMAIL_USER -> EmailUserFavouriteTable
                        UserType.PASSKEY_USER -> PasskeyUserFavouriteTable
                    },
                    joinType = JoinType.INNER,
                    additionalConstraint = {
                        when (userType) {
                            UserType.GOOGLE_USER -> GoogleUserFavouriteTable.songId
                            UserType.EMAIL_USER -> EmailUserFavouriteTable.songId
                            UserType.PASSKEY_USER -> PasskeyUserFavouriteTable.songId
                        } as Column<*> eq SongTable.id
                    }
                )
                .slice(
                    SongTable.id,
                    SongTable.title,
                    SongTable.album,
                    SongTable.artist,
                    SongTable.coverImage,
                    SongTable.masterPlaylistPath,
                    SongTable.totalTime,
                    SongTable.genre,
                    SongTable.publisher,
                    SongTable.composer,
                    SongTable.album_artist,
                    SongTable.description,
                    SongTable.track,
                    SongTable.date,
                )
                .select {
                    when (userType) {
                        UserType.GOOGLE_USER -> GoogleUserFavouriteTable.userId
                        UserType.EMAIL_USER -> EmailUserFavouriteTable.userId
                        UserType.PASSKEY_USER -> PasskeyUserFavouriteTable.userId
                    } eq userId
                }.orderBy(
                    when (userType) {
                        UserType.GOOGLE_USER -> GoogleUserFavouriteTable.date
                        UserType.EMAIL_USER -> EmailUserFavouriteTable.date
                        UserType.PASSKEY_USER -> PasskeyUserFavouriteTable.date
                    }, SortOrder.DESC
                ).map {
                    it.toResponseSong()
                }
        }
    )

    override suspend fun isOldEnough(userId: Long, userType: UserType): Boolean = dbQuery {
        when (userType) {
            UserType.GOOGLE_USER -> {
                GoogleUserListenHistoryTable.select {
                    GoogleUserListenHistoryTable.userId eq userId
                }.count() > 14
            }

            UserType.EMAIL_USER -> {
                EmailUserListenHistoryTable.select {
                    EmailUserListenHistoryTable.userId eq userId
                }.count() > 14
            }

            UserType.PASSKEY_USER -> {
                PasskeyUserListenHistoryTable.select {
                    PasskeyUserListenHistoryTable.userId eq userId
                }.count() > 14
            }
        }
    }

    // getFevArtistMixPrev
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

    // getAlbumPrev
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
                AlbumTable.id,
                SongTable.id,
                SongTable.title,
                SongTable.coverImage,
                SongTable.artist,
                SongTable.album
            ).select {
                AlbumTable.id inList this@getAlbumOnAlbumIdList
            }.orderBy(AlbumTable.points, SortOrder.ASC)
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
            }.groupBy {
                it.albumId
            }.map {
                AlbumPreview(
                    id = it.key,
                    name = it.value[0].name,
                    listOfSongs = it.value.toPreviewSong()
                )
            }
    }

    // getArtistPrev
    private suspend fun getHistoryArtistIdList(
        userType: UserType,
        usedId: Long
    ) = dbQuery {
        when (userType) {
            UserType.GOOGLE_USER -> {
                GoogleUserListenHistoryTable
                    .slice(
                        GoogleUserListenHistoryTable.songId,
                        GoogleUserListenHistoryTable.repeat
                    )
                    .select {
                        GoogleUserListenHistoryTable.userId eq usedId and (
                                GoogleUserListenHistoryTable.date greaterEq (
                                        LocalDateTime.now().minus(1, ChronoUnit.DAYS)
                                        )
                                )
                    }.map {
                        Pair(
                            first = it[GoogleUserListenHistoryTable.songId],
                            second = it[GoogleUserListenHistoryTable.repeat]
                        )
                    }
            }

            UserType.EMAIL_USER -> {
                EmailUserListenHistoryTable
                    .slice(
                        EmailUserListenHistoryTable.songId,
                        EmailUserListenHistoryTable.repeat
                    )
                    .select {
                        EmailUserListenHistoryTable.userId eq usedId and (
                                EmailUserListenHistoryTable.date greaterEq (
                                        LocalDateTime.now().minus(1, ChronoUnit.DAYS)
                                        )
                                )
                    }.map {
                        Pair(
                            first = it[EmailUserListenHistoryTable.songId],
                            second = it[EmailUserListenHistoryTable.repeat]
                        )
                    }
            }

            UserType.PASSKEY_USER -> {
                PasskeyUserListenHistoryTable
                    .slice(
                        PasskeyUserListenHistoryTable.songId,
                        PasskeyUserListenHistoryTable.repeat
                    )
                    .select {
                        PasskeyUserListenHistoryTable.userId eq usedId and (
                                PasskeyUserListenHistoryTable.date greaterEq (
                                        LocalDateTime.now().minus(1, ChronoUnit.DAYS)
                                        )
                                )
                    }.map {
                        Pair(
                            first = it[PasskeyUserListenHistoryTable.songId],
                            second = it[PasskeyUserListenHistoryTable.repeat]
                        )
                    }
            }
        }.groupBy(
            keySelector = { it.first },
            valueTransform = { it.second }
        ).map { (key, values) ->
            key to (values.sum() + values.size)
        }.sortedByDescending {
            it.second
        }.take(2)
            .map { it.first }
            .let {
                SongArtistRelation.find {
                    SongArtistRelationTable.songId inList it
                }.map {
                    it.artistId
                }
            }
    }

    private suspend fun getFevArtistIdList(
        userType: UserType,
        usedId: Long
    ) = dbQuery {
        when (userType) {
            UserType.GOOGLE_USER -> {
                GoogleUserArtistRelation.find {
                    GoogleUserArtistRelationTable.userId eq usedId
                }.map {
                    it.artistId
                }
            }

            UserType.EMAIL_USER -> {
                EmailUserArtistRelation.find {
                    EmailUserArtistRelationTable.userId eq usedId
                }.map {
                    it.artistId
                }
            }

            UserType.PASSKEY_USER -> {
                PasskeyUserArtistRelation.find {
                    PasskeyUserArtistRelationTable.userId eq usedId
                }.map {
                    it.artistId
                }
            }
        }
    }

    private suspend fun List<Int>.getResponseArtistPreviewOnArtistIdList() = dbQuery {
        SongTable
            .join(
                otherTable = ArtistTable,
                joinType = JoinType.INNER,
                additionalConstraint = {
                    SongTable.artist eq ArtistTable.name
                }
            ).slice(
                SongTable.id,
                SongTable.title,
                SongTable.coverImage,
                SongTable.artist,
                SongTable.album,
                SongTable.points,
                ArtistTable.id,
                ArtistTable.profilePicUrl
            ).select {
                ArtistTable.id inList this@getResponseArtistPreviewOnArtistIdList
            }.mapToResponseArtistsPreview()
    }

    private fun Query.mapToResponseArtistsPreview() = this.map {
        DbResponseArtistPreview(
            songId = it[SongTable.id].value.toString(),
            songTitle = it[SongTable.title],
            songCover = it[SongTable.coverImage].constructCoverPhotoUrl(),
            artist = it[SongTable.artist],
            album = it[SongTable.album],
            artistId = it[ArtistTable.id].value,
            artistImage = it[ArtistTable.profilePicUrl]
        )
    }.groupBy {
        it.artist
    }.map {
        ResponseArtistsPreview(
            artist = ResponseArtist(
                id = it.value[0].artistId,
                name = it.key,
                imageUrl = ResponseArtist.getArtistImageUrl(it.value[0].artistImage)
            ),
            listOfSongs = it.value.toListOfSongPreview().take(5)
        )
    }

    private suspend fun getHistorySongIdList(userType: UserType, userId: Long) = dbQuery {
        when (userType) {
            UserType.GOOGLE_USER -> {
                val subQueryMaxDate = GoogleUserListenHistoryTable
                    .slice(GoogleUserListenHistoryTable.date.max())
                    .select {
                        GoogleUserListenHistoryTable.userId eq userId
                    }.single()[GoogleUserListenHistoryTable.date.max()]

                if (subQueryMaxDate == null) return@dbQuery emptyList()


                val threeDaysAgo = subQueryMaxDate.minus(3, TimeUnit.DAYS.toChronoUnit())

                GoogleUserListenHistoryTable
                    .slice(
                        GoogleUserListenHistoryTable.songId
                    ).select {
                        GoogleUserListenHistoryTable.userId eq userId and (
                                GoogleUserListenHistoryTable.date greaterEq threeDaysAgo
                                )
                    }
                    .withDistinct()
                    .orderBy(org.jetbrains.exposed.sql.Random())
                    .limit(8)
                    .map {
                        it[GoogleUserListenHistoryTable.songId]
                    }
            }

            UserType.EMAIL_USER -> {
                val subQueryMaxDate = EmailUserListenHistoryTable
                    .slice(EmailUserListenHistoryTable.date.max())
                    .select {
                        EmailUserListenHistoryTable.userId eq userId
                    }.single()[EmailUserListenHistoryTable.date.max()]

                if (subQueryMaxDate == null) return@dbQuery emptyList()


                val threeDaysAgo = subQueryMaxDate.minus(3, TimeUnit.DAYS.toChronoUnit())


                EmailUserListenHistoryTable
                    .slice(
                        EmailUserListenHistoryTable.songId
                    ).select {
                        EmailUserListenHistoryTable.userId eq userId and (
                                EmailUserListenHistoryTable.date greaterEq threeDaysAgo
                                )
                    }
                    .withDistinct()
                    .orderBy(org.jetbrains.exposed.sql.Random())
                    .limit(8)
                    .map {
                        it[EmailUserListenHistoryTable.songId]
                    }
            }

            UserType.PASSKEY_USER -> {
                val subQueryMaxDate = PasskeyUserListenHistoryTable
                    .slice(PasskeyUserListenHistoryTable.date.max())
                    .select {
                        PasskeyUserListenHistoryTable.userId eq userId
                    }.single()[PasskeyUserListenHistoryTable.date.max()]

                if (subQueryMaxDate == null) return@dbQuery emptyList()


                val threeDaysAgo = subQueryMaxDate.minus(3, TimeUnit.DAYS.toChronoUnit())


                PasskeyUserListenHistoryTable
                    .slice(
                        PasskeyUserListenHistoryTable.songId
                    ).select {
                        PasskeyUserListenHistoryTable.userId eq userId and (
                                PasskeyUserListenHistoryTable.date greaterEq threeDaysAgo
                                )
                    }
                    .withDistinct()
                    .orderBy(org.jetbrains.exposed.sql.Random())
                    .limit(8)
                    .map {
                        it[PasskeyUserListenHistoryTable.songId]
                    }
            }
        }
    }

    private suspend fun getPreviewSongsByTheArtists(songIdList: List<Long>) = dbQuery {
        val sar1 = SongArtistRelationTable.alias("sar1")
        val sar2 = SongArtistRelationTable.alias("sar2")

        SongTable
            .join(
                otherTable = sar1,
                joinType = JoinType.INNER,
                additionalConstraint = {
                    SongTable.id eq sar1[SongArtistRelationTable.songId] as Column<*>
                }
            ).join(
                otherTable = ArtistTable,
                joinType = JoinType.INNER,
                additionalConstraint = {
                    ArtistTable.id eq sar1[SongArtistRelationTable.artistId] as Column<*>
                }
            ).join(
                otherTable = sar2,
                joinType = JoinType.INNER,
                additionalConstraint = {
                    ArtistTable.id eq sar2[SongArtistRelationTable.artistId] as Column<*>
                }
            ).slice(
                SongTable.id,
                SongTable.title,
                SongTable.coverImage,
                SongTable.artist,
                SongTable.album,
                SongTable.points,
                SongTable.date
            ).select {
                sar2[SongArtistRelationTable.songId] inList songIdList
            }.orderBy(SongTable.points, SortOrder.DESC)
            .map {
                SongPreview(
                    id = it[SongTable.id].toString(),
                    title = it[SongTable.title],
                    artist = it[SongTable.artist],
                    album = it[SongTable.album],
                    coverImage = it[SongTable.coverImage].constructCoverPhotoUrl(),
                    points = it[SongTable.points],
                    year = it[SongTable.date]
                )
            }.groupBy {
                it.artist
            }
    }

    private suspend fun getPinnedPlaylist(userId: Long, userType: UserType) = dbQuery {
        when (userType) {
            UserType.GOOGLE_USER -> {
                GoogleUserPinnedPlaylistTable.slice(
                    GoogleUserPinnedPlaylistTable.playlistId
                ).select {
                    GoogleUserPinnedPlaylistTable.userId eq userId
                }.map {
                    it[GoogleUserPinnedPlaylistTable.playlistId]
                }.map {
                    Pinned(
                        id = it,
                        type = IdType.PLAYLIST
                    )
                }
            }

            UserType.EMAIL_USER -> {
                EmailUserPinnedPlaylistTable.slice(
                    EmailUserPinnedPlaylistTable.playlistId
                ).select {
                    EmailUserPinnedPlaylistTable.userId eq userId
                }.map {
                    it[EmailUserPinnedPlaylistTable.playlistId]
                }.map {
                    Pinned(
                        id = it,
                        type = IdType.PLAYLIST
                    )
                }
            }

            UserType.PASSKEY_USER -> {
                PasskeyUserPinnedPlaylistTable.slice(
                    PasskeyUserPinnedPlaylistTable.playlistId
                ).select {
                    PasskeyUserPinnedPlaylistTable.userId eq userId
                }.map {
                    it[PasskeyUserPinnedPlaylistTable.playlistId]
                }.map {
                    Pinned(
                        id = it,
                        type = IdType.PLAYLIST
                    )
                }
            }
        }
    }

    private suspend fun getPinnedAlbum(userType: UserType, userId: Long) = dbQuery {
        when (userType) {
            UserType.GOOGLE_USER -> {
                GoogleUserPinnedAlbumTable.slice(
                    GoogleUserPinnedAlbumTable.albumId
                ).select {
                    GoogleUserPinnedAlbumTable.userId eq userId
                }.map {
                    Pinned(
                        id = it[GoogleUserPinnedAlbumTable.albumId],
                        type = IdType.ALBUM
                    )
                }
            }

            UserType.EMAIL_USER -> {
                EmailUserPinnedAlbumTable.slice(
                    EmailUserPinnedAlbumTable.albumId
                ).select {
                    EmailUserPinnedAlbumTable.userId eq userId
                }.map {
                    Pinned(
                        id = it[EmailUserPinnedAlbumTable.albumId],
                        type = IdType.ALBUM
                    )
                }
            }

            UserType.PASSKEY_USER -> {
                PasskeyUserPinnedAlbumTable.slice(
                    PasskeyUserPinnedAlbumTable.albumId
                ).select {
                    PasskeyUserPinnedAlbumTable.userId eq userId
                }.map {
                    Pinned(
                        id = it[PasskeyUserPinnedAlbumTable.albumId],
                        type = IdType.ALBUM
                    )
                }
            }
        }
    }

    private suspend fun getPinnedArtist(userType: UserType, userId: Long) = dbQuery {
        when (userType) {
            UserType.GOOGLE_USER -> {
                GoogleUserPinnedArtistTable.slice(
                    GoogleUserPinnedArtistTable.artistId
                ).select {
                    GoogleUserPinnedArtistTable.userId eq userId
                }.map {
                    Pinned(
                        id = it[GoogleUserPinnedArtistTable.artistId].toLong(),
                        type = IdType.ARTIST
                    )
                }
            }

            UserType.EMAIL_USER -> {
                EmailUserPinnedArtistTable.slice(
                    EmailUserPinnedArtistTable.artistId
                ).select {
                    EmailUserPinnedArtistTable.userId eq userId
                }.map {
                    Pinned(
                        id = it[EmailUserPinnedArtistTable.artistId].toLong(),
                        type = IdType.ARTIST
                    )
                }
            }

            UserType.PASSKEY_USER -> {
                PasskeyUserPinnedArtistTable.slice(
                    PasskeyUserPinnedArtistTable.artistId
                ).select {
                    PasskeyUserPinnedArtistTable.userId eq userId
                }.map {
                    Pinned(
                        id = it[PasskeyUserPinnedArtistTable.artistId].toLong(),
                        type = IdType.ARTIST
                    )
                }
            }
        }
    }
}