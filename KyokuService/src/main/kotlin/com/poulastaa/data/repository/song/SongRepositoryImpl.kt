package com.poulastaa.data.repository.song

import com.poulastaa.data.model.common.IdType
import com.poulastaa.data.model.common.ResponseSong
import com.poulastaa.data.model.db_table.ArtistTable
import com.poulastaa.data.model.db_table.SongArtistRelationTable
import com.poulastaa.data.model.db_table.SongGenreRelationTable
import com.poulastaa.data.model.db_table.SongTable
import com.poulastaa.data.model.db_table.user_fev.EmailUserFavouriteTable
import com.poulastaa.data.model.db_table.user_fev.GoogleUserFavouriteTable
import com.poulastaa.data.model.db_table.user_fev.PasskeyUserFavouriteTable
import com.poulastaa.data.model.db_table.user_genre.EmailUserGenreRelationTable
import com.poulastaa.data.model.db_table.user_genre.GoogleUserGenreRelationTable
import com.poulastaa.data.model.db_table.user_genre.PasskeyUserGenreRelationTable
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
import com.poulastaa.data.model.home.DailyMixPreview
import com.poulastaa.data.model.home.SongPreview
import com.poulastaa.data.model.pinned.PinnedOperation
import com.poulastaa.data.model.pinned.PinnedReq
import com.poulastaa.data.model.setup.spotify.*
import com.poulastaa.data.model.utils.UserType
import com.poulastaa.data.model.utils.UserTypeHelper
import com.poulastaa.domain.dao.Song
import com.poulastaa.domain.dao.playlist.Playlist
import com.poulastaa.domain.repository.song.SongRepository
import com.poulastaa.plugins.dbQuery
import com.poulastaa.utils.constructCoverPhotoUrl
import com.poulastaa.utils.constructMasterPlaylistUrl
import com.poulastaa.utils.toResponseSong
import com.poulastaa.utils.toResponseSongList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.sql.SQLException
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit
import kotlin.random.Random
import org.jetbrains.exposed.sql.Random as SqlRandom

class SongRepositoryImpl : SongRepository {
    override suspend fun handleSpotifyPlaylist(list: List<SpotifySong>): HandleSpotifyPlaylist {
        val foundSongs = ConcurrentHashMap<Long, Song>()

        return try {
            val playlist = dbQuery {
                list.forEach { spotifySong ->
                    Song.find {// searching by title and album
                        SongTable.title like "%${spotifySong.title}%" and (SongTable.album like "%${spotifySong.album}%")
                    }.forEach {
                        foundSongs.putIfAbsent(it.id.value, it)
                    }

                    Song.find {// searching by title
                        SongTable.title like "%${spotifySong.title}%"
                    }.forEach {
                        if (
                            foundSongs.putIfAbsent(it.id.value, it) == null &&
                            spotifySong.album!!.contains(it.album)
                        ) foundSongs[it.id.value] = it
                    }
                }

                dbQuery {
                    Playlist.new {
                        this.name = "Playlist #${Random.nextInt(1000, 99999)}"
                    }
                }
            }

            HandleSpotifyPlaylist(
                status = HandleSpotifyPlaylistStatus.SUCCESS,
                playlist = playlist,
                spotifyPlaylistResponse = SpotifyPlaylistResponse(
                    status = HandleSpotifyPlaylistStatus.SUCCESS,
                    id = playlist.id.value,
                    name = playlist.name,
                    listOfResponseSong = foundSongs.values.toResponseSongList()
                ),
                spotifySongDownloaderApiReq = SpotifySongDownloaderApiReq(
                    listOfSong = notFoundSongs(list, foundSongs.values.toResponseSongList())
                ),
                listOfSongs = foundSongs.values.toList()
            )
        } catch (e: Exception) {
            HandleSpotifyPlaylist(
                status = HandleSpotifyPlaylistStatus.FAILURE
            )
        }
    }

    override suspend fun getDailyMixPreview(helper: UserTypeHelper): DailyMixPreview {
        val historySongIdList = getHistorySongIdList(helper.userType, helper.id) // get artistId from history
        val songsByTheArtistUnSorted = getPreviewSongsByTheArtists(historySongIdList)

        return DailyMixPreview(
            listOfSongs = songsByTheArtistUnSorted.flatMap {
                it.value.take(10)
            }.distinctBy {
                it.id
            }.shuffled(java.util.Random()).take(4).map {
                SongPreview(
                    id = it.id.toString(),
                    title = it.title,
                    artist = it.artist,
                    album = it.album,
                    coverImage = it.coverImage,
                    year = it.date
                )
            }
        )
    }

    override suspend fun getDailyMix(helper: UserTypeHelper) = withContext(Dispatchers.IO) {
        val subResponseOneDeferred = async {
            val historySongIdList = getHistorySongIdList(helper.userType, helper.id)

            val historySongList = historySongIdList.getHistorySongList()

            // get songs based on artists of users last listened songs
            val songsByTheArtistUnSorted = getPreviewSongsByTheArtists(historySongIdList)

            val songByArtistSorted = songsByTheArtistUnSorted
                .flatMap {
                    it.value.take(songsByTheArtistUnSorted.size / 30)
                }.distinctBy {
                    it.id
                }.filterNot { songByArtist -> // three may be repeats so remove if any
                    historySongList.any {
                        it.id == songByArtist.id
                    }
                }.shuffled(java.util.Random()).take(Random(10).nextInt(25, 30))

            songByArtistSorted + historySongList
        }

        // get songs based on user genre
        val subResponseTwoDeferred = async {
            helper.id.getSongByGenre(helper.userType)
        }

        val one = subResponseOneDeferred.await()
        val two = subResponseTwoDeferred.await()
            .filterNot { song -> // remove one from two
                one.any {
                    it.id == song.id
                }
            }.take(12)

        (one + two).shuffled(java.util.Random((one.size + two.size).toLong()))
    }


    override suspend fun handlePinnedOperation(
        userId: Long,
        userType: UserType,
        req: PinnedReq
    ): Boolean = dbQuery {
        when (userType) {
            UserType.GOOGLE_USER -> {
                when (req.type) {
                    IdType.PLAYLIST -> {
                        when (req.operation) {
                            PinnedOperation.ADD -> {
                                try {
                                    GoogleUserPinnedPlaylistTable.insert {
                                        it[this.userId] = userId
                                        it[this.playlistId] = req.id
                                    }
                                    return@dbQuery true
                                } catch (e: SQLException) {
                                    return@dbQuery e.errorCode == 1062 // duplicate entry
                                } catch (e: Exception) {
                                    return@dbQuery false
                                }
                            }

                            PinnedOperation.REMOVE -> {
                                try {
                                    GoogleUserPinnedPlaylistTable.deleteWhere {
                                        this.playlistId eq req.id
                                    }
                                    return@dbQuery true
                                } catch (e: Exception) {
                                    return@dbQuery false
                                }
                            }

                            PinnedOperation.ERR -> return@dbQuery false
                        }
                    }

                    IdType.ALBUM -> {
                        when (req.operation) {
                            PinnedOperation.ADD -> {
                                try {
                                    GoogleUserPinnedAlbumTable.insert {
                                        it[this.userId] = userId
                                        it[this.albumId] = req.id
                                    }
                                    return@dbQuery true
                                } catch (e: SQLException) {
                                    return@dbQuery e.errorCode == 1062
                                } catch (e: Exception) {
                                    return@dbQuery false
                                }
                            }

                            PinnedOperation.REMOVE -> {
                                try {
                                    GoogleUserPinnedAlbumTable.deleteWhere {
                                        this.albumId eq req.id
                                    }
                                    return@dbQuery true
                                } catch (e: Exception) {
                                    return@dbQuery false
                                }
                            }

                            PinnedOperation.ERR -> return@dbQuery false
                        }
                    }

                    IdType.ARTIST -> {
                        when (req.operation) {
                            PinnedOperation.ADD -> {
                                try {
                                    GoogleUserPinnedArtistTable.insert {
                                        it[this.userId] = userId
                                        it[this.artistId] = req.id.toInt()
                                    }
                                    return@dbQuery true
                                } catch (e: SQLException) {
                                    return@dbQuery e.errorCode == 1062
                                } catch (e: Exception) {
                                    return@dbQuery false
                                }
                            }

                            PinnedOperation.REMOVE -> {
                                try {
                                    GoogleUserPinnedArtistTable.deleteWhere {
                                        this.artistId eq req.id.toInt()
                                    }
                                    return@dbQuery true
                                } catch (e: Exception) {
                                    return@dbQuery false
                                }
                            }

                            PinnedOperation.ERR -> return@dbQuery false
                        }
                    }

                    IdType.ERR -> return@dbQuery false
                }
            }

            UserType.EMAIL_USER -> {
                when (req.type) {
                    IdType.PLAYLIST -> {
                        when (req.operation) {
                            PinnedOperation.ADD -> {
                                try {
                                    EmailUserPinnedPlaylistTable.insert {
                                        it[this.userId] = userId
                                        it[this.playlistId] = req.id
                                    }
                                    return@dbQuery true
                                } catch (e: SQLException) {
                                    return@dbQuery e.errorCode == 1062
                                } catch (e: Exception) {
                                    return@dbQuery false
                                }
                            }

                            PinnedOperation.REMOVE -> {
                                try {
                                    EmailUserPinnedPlaylistTable.deleteWhere {
                                        this.playlistId eq req.id
                                    }
                                    return@dbQuery true
                                } catch (e: Exception) {
                                    return@dbQuery false
                                }
                            }

                            PinnedOperation.ERR -> return@dbQuery false
                        }
                    }

                    IdType.ALBUM -> {
                        when (req.operation) {
                            PinnedOperation.ADD -> {
                                try {
                                    EmailUserPinnedAlbumTable.insert {
                                        it[this.userId] = userId
                                        it[this.albumId] = req.id
                                    }
                                    return@dbQuery true
                                } catch (e: SQLException) {
                                    return@dbQuery e.errorCode == 1062
                                } catch (e: Exception) {
                                    return@dbQuery false
                                }
                            }

                            PinnedOperation.REMOVE -> {
                                try {
                                    EmailUserPinnedAlbumTable.deleteWhere {
                                        this.albumId eq req.id
                                    }
                                    return@dbQuery true
                                } catch (e: Exception) {
                                    return@dbQuery false
                                }
                            }

                            PinnedOperation.ERR -> return@dbQuery false
                        }
                    }

                    IdType.ARTIST -> {
                        when (req.operation) {
                            PinnedOperation.ADD -> {
                                try {
                                    EmailUserPinnedArtistTable.insert {
                                        it[this.userId] = userId
                                        it[this.artistId] = req.id.toInt()
                                    }
                                    return@dbQuery true
                                } catch (e: SQLException) {
                                    return@dbQuery e.errorCode == 1062
                                } catch (e: Exception) {
                                    return@dbQuery false
                                }
                            }

                            PinnedOperation.REMOVE -> {
                                try {
                                    EmailUserPinnedArtistTable.deleteWhere {
                                        this.artistId eq req.id.toInt()
                                    }
                                    return@dbQuery true
                                } catch (e: Exception) {
                                    return@dbQuery false
                                }
                            }

                            PinnedOperation.ERR -> return@dbQuery false
                        }
                    }

                    IdType.ERR -> return@dbQuery false
                }
            }

            UserType.PASSKEY_USER -> {
                when (req.type) {
                    IdType.PLAYLIST -> {
                        when (req.operation) {
                            PinnedOperation.ADD -> {
                                try {
                                    PasskeyUserPinnedPlaylistTable.insert {
                                        it[this.userId] = userId
                                        it[this.playlistId] = req.id
                                    }
                                    return@dbQuery true
                                } catch (e: SQLException) {
                                    return@dbQuery e.errorCode == 1062
                                } catch (e: Exception) {
                                    return@dbQuery false
                                }
                            }

                            PinnedOperation.REMOVE -> {
                                try {
                                    PasskeyUserPinnedPlaylistTable.deleteWhere {
                                        this.playlistId eq req.id
                                    }
                                    return@dbQuery true
                                } catch (e: Exception) {
                                    return@dbQuery false
                                }
                            }

                            PinnedOperation.ERR -> return@dbQuery false
                        }
                    }

                    IdType.ALBUM -> {
                        when (req.operation) {
                            PinnedOperation.ADD -> {
                                try {
                                    PasskeyUserPinnedAlbumTable.insert {
                                        it[this.userId] = userId
                                        it[this.albumId] = req.id
                                    }
                                    return@dbQuery true
                                } catch (e: SQLException) {
                                    return@dbQuery e.errorCode == 1062
                                } catch (e: Exception) {
                                    return@dbQuery false
                                }
                            }

                            PinnedOperation.REMOVE -> {
                                try {
                                    PasskeyUserPinnedAlbumTable.deleteWhere {
                                        this.albumId eq req.id
                                    }
                                    return@dbQuery true
                                } catch (e: Exception) {
                                    return@dbQuery false
                                }
                            }

                            PinnedOperation.ERR -> return@dbQuery false
                        }
                    }

                    IdType.ARTIST -> {
                        when (req.operation) {
                            PinnedOperation.ADD -> {
                                try {
                                    PasskeyUserPinnedArtistTable.insert {
                                        it[this.userId] = userId
                                        it[this.artistId] = req.id.toInt()
                                    }
                                    return@dbQuery true
                                } catch (e: SQLException) {
                                    return@dbQuery e.errorCode == 1062
                                } catch (e: Exception) {
                                    return@dbQuery false
                                }
                            }

                            PinnedOperation.REMOVE -> {
                                try {
                                    PasskeyUserPinnedArtistTable.deleteWhere {
                                        this.artistId eq req.id.toInt()
                                    }
                                    return@dbQuery true
                                } catch (e: Exception) {
                                    return@dbQuery false
                                }
                            }

                            PinnedOperation.ERR -> return@dbQuery false
                        }
                    }

                    IdType.ERR -> return@dbQuery false
                }
            }
        }
    }

    override suspend fun getResponseSongOnId(listOfId: List<Long>): List<ResponseSong> = dbQuery {
        Song.find {
            SongTable.id inList listOfId
        }.map {
            it.toResponseSong()
        }
    }

    override suspend fun getResponseSong(songId: Long): ResponseSong = dbQuery {
        Song.find {
            SongTable.id eq songId
        }.firstOrNull()?.toResponseSong() ?: ResponseSong()
    }

    override suspend fun handleFavouriteOperation(
        userType: UserType,
        userId: Long,
        songId: Long,
        operation: SongRepository.FavouriteOperation
    ) {
        withContext(Dispatchers.IO) {
            when (operation) {
                SongRepository.FavouriteOperation.ADD -> dbQuery {
                    when (userType) {
                        UserType.GOOGLE_USER -> {
                            GoogleUserFavouriteTable.insertIgnore {
                                it[this.userId] = userId
                                it[this.songId] = songId
                            }
                        }

                        UserType.EMAIL_USER -> {
                            EmailUserFavouriteTable.insertIgnore {
                                it[this.userId] = userId
                                it[this.songId] = songId
                            }
                        }

                        UserType.PASSKEY_USER -> {
                            PasskeyUserFavouriteTable.insertIgnore {
                                it[this.userId] = userId
                                it[this.songId] = songId
                            }
                        }
                    }

                    return@dbQuery
                }

                SongRepository.FavouriteOperation.REMOVE -> dbQuery {
                    when (userType) {
                        UserType.GOOGLE_USER -> {
                            GoogleUserFavouriteTable.deleteWhere {
                                GoogleUserFavouriteTable.songId eq songId and
                                        (GoogleUserFavouriteTable.userId eq userId)
                            }
                        }

                        UserType.EMAIL_USER -> {
                            EmailUserFavouriteTable.deleteWhere {
                                EmailUserFavouriteTable.userId eq userId and
                                        (EmailUserFavouriteTable.songId eq songId)
                            }
                        }

                        UserType.PASSKEY_USER -> {
                            PasskeyUserFavouriteTable.deleteWhere {
                                PasskeyUserFavouriteTable.userId eq userId and
                                        (PasskeyUserFavouriteTable.songId eq songId)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun notFoundSongs(
        list: List<SpotifySong>,
        responseSong: List<ResponseSong>
    ): List<SpotifySong> {
        val setOfResponseTitles = responseSong.map { it.title }.toSet()

        return list.filter { spotifySong ->
            spotifySong.title?.let { title ->
                !setOfResponseTitles.any { responseTitle -> responseTitle.contains(title) }
            } ?: false
        }
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
                EmailUserListenHistoryTable
                    .slice(
                        EmailUserListenHistoryTable.songId
                    ).select {
                        EmailUserListenHistoryTable.userId eq userId and (
                                EmailUserListenHistoryTable.date greaterEq (
                                        LocalDateTime.now().minus(3, ChronoUnit.DAYS)
                                        )
                                )
                    }
                    .withDistinct()
                    .orderBy(SqlRandom())
                    .limit(8)
                    .map {
                        it[EmailUserListenHistoryTable.songId]
                    }
            }

            UserType.PASSKEY_USER -> {
                PasskeyUserListenHistoryTable
                    .slice(
                        PasskeyUserListenHistoryTable.songId
                    ).select {
                        PasskeyUserListenHistoryTable.userId eq userId and (
                                PasskeyUserListenHistoryTable.date greaterEq (
                                        LocalDateTime.now().minus(3, ChronoUnit.DAYS)
                                        )
                                )
                    }
                    .withDistinct()
                    .orderBy(SqlRandom())
                    .limit(8)
                    .map {
                        it[PasskeyUserListenHistoryTable.songId]
                    }
            }
        }
    }

    private suspend fun List<Long>.getHistorySongList() = dbQuery {
        Song.find {
            SongTable.id inList this@getHistorySongList
        }.map {
            ResponseSong(
                id = it.id.value,
                title = it.title,
                artist = it.artist,
                album = it.album,
                coverImage = it.coverImage.constructCoverPhotoUrl(),
                masterPlaylistUrl = it.masterPlaylistPath.constructMasterPlaylistUrl(),
                totalTime = it.totalTime,
                date = it.date
            )
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
                SongTable.artist,
                SongTable.album,
                SongTable.coverImage,
                SongTable.masterPlaylistPath,
                SongTable.totalTime,
                SongTable.date
            ).select {
                sar2[SongArtistRelationTable.songId] inList songIdList
            }.orderBy(SongTable.points, SortOrder.DESC)
            .map {
                ResponseSong(
                    id = it[SongTable.id].value,
                    title = it[SongTable.title],
                    artist = it[SongTable.artist],
                    album = it[SongTable.album],
                    coverImage = it[SongTable.coverImage].constructCoverPhotoUrl(),
                    masterPlaylistUrl = it[SongTable.masterPlaylistPath].constructMasterPlaylistUrl(),
                    totalTime = it[SongTable.totalTime],
                    date = it[SongTable.date]
                )
            }.groupBy {
                it.artist
            }
    }

    private suspend fun Long.getSongByGenre(userType: UserType) = dbQuery {
        SongTable.join(
            SongGenreRelationTable,
            JoinType.INNER,
            additionalConstraint = {
                SongGenreRelationTable.songId as Column<*> eq SongTable.id
            }
        ).join(userType, this)
            .orderBy(SongTable.points, SortOrder.DESC)
            .map {
                ResponseSong(
                    id = it[SongTable.id].value,
                    title = it[SongTable.title],
                    artist = it[SongTable.artist],
                    album = it[SongTable.album],
                    coverImage = it[SongTable.coverImage].constructCoverPhotoUrl(),
                    masterPlaylistUrl = it[SongTable.masterPlaylistPath].constructMasterPlaylistUrl(),
                    totalTime = it[SongTable.totalTime],
                    date = it[SongTable.date]
                )
            }
    }

    private fun Join.join(userType: UserType, userId: Long) = when (userType) {
        UserType.GOOGLE_USER -> {
            this.join(
                GoogleUserGenreRelationTable,
                JoinType.INNER,
                additionalConstraint = {
                    SongGenreRelationTable.genreId eq GoogleUserGenreRelationTable.genreId
                }
            ).slice(
                SongTable.id,
                SongTable.title,
                SongTable.artist,
                SongTable.album,
                SongTable.coverImage,
                SongTable.masterPlaylistPath,
                SongTable.totalTime,
                SongTable.date
            ).select {
                GoogleUserGenreRelationTable.userId eq userId
            }
        }

        UserType.EMAIL_USER -> {
            this.join(
                EmailUserGenreRelationTable,
                JoinType.INNER,
                additionalConstraint = {
                    SongGenreRelationTable.genreId eq EmailUserGenreRelationTable.genreId
                }
            ).slice(
                SongTable.id,
                SongTable.title,
                SongTable.artist,
                SongTable.album,
                SongTable.coverImage,
                SongTable.masterPlaylistPath,
                SongTable.totalTime,
                SongTable.date
            ).select {
                EmailUserGenreRelationTable.userId eq userId
            }
        }

        UserType.PASSKEY_USER -> {
            this.join(
                PasskeyUserGenreRelationTable,
                JoinType.INNER,
                additionalConstraint = {
                    SongGenreRelationTable.genreId eq PasskeyUserGenreRelationTable.genreId
                }
            ).slice(
                SongTable.id,
                SongTable.title,
                SongTable.artist,
                SongTable.album,
                SongTable.coverImage,
                SongTable.masterPlaylistPath,
                SongTable.totalTime,
                SongTable.date
            ).select {
                PasskeyUserGenreRelationTable.userId eq userId
            }
        }
    }
}