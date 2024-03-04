package com.poulastaa.data.repository.song

import com.poulastaa.data.model.common.ResponseSong
import com.poulastaa.data.model.db_table.ArtistTable
import com.poulastaa.data.model.db_table.SongArtistRelationTable
import com.poulastaa.data.model.db_table.SongGenreRelationTable
import com.poulastaa.data.model.db_table.SongTable
import com.poulastaa.data.model.db_table.user_genre.EmailUserGenreRelationTable
import com.poulastaa.data.model.db_table.user_genre.GoogleUserGenreRelationTable
import com.poulastaa.data.model.db_table.user_genre.PasskeyUserGenreRelationTable
import com.poulastaa.data.model.db_table.user_listen_history.EmailUserListenHistoryTable
import com.poulastaa.data.model.db_table.user_listen_history.GoogleUserListenHistoryTable
import com.poulastaa.data.model.db_table.user_listen_history.PasskeyUserListenHistoryTable
import com.poulastaa.data.model.home.DailyMixPreview
import com.poulastaa.data.model.home.HomeResponseSong
import com.poulastaa.data.model.setup.spotify.*
import com.poulastaa.data.model.utils.UserType
import com.poulastaa.data.model.utils.UserTypeHelper
import com.poulastaa.domain.dao.Song
import com.poulastaa.domain.repository.song.SongRepository
import com.poulastaa.plugins.dbQuery
import com.poulastaa.utils.toResponseSongList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.*
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.concurrent.ConcurrentHashMap
import kotlin.random.Random
import org.jetbrains.exposed.sql.Random as SqlRandom

class SongRepositoryImpl : SongRepository {
    override suspend fun handleSpotifyPlaylist(list: List<SpotifySong>): HandleSpotifyPlaylist {
        val foundSongs = ConcurrentHashMap<Long, Song>()

        return try {
            dbQuery {
                list.forEach { spotifySong ->
                    Song.find { // searching by title and album
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
            }

            HandleSpotifyPlaylist(
                status = HandleSpotifyPlaylistStatus.SUCCESS,
                spotifyPlaylistResponse = SpotifyPlaylistResponse(
                    status = HandleSpotifyPlaylistStatus.SUCCESS,
                    name = "Playlist #${Random.nextInt(1000, 99999)}",
                    listOfResponseSong = foundSongs.values.toResponseSongList()
                ),
                spotifySongDownloaderApiReq = SpotifySongDownloaderApiReq(
                    listOfSong = notFoundSongs(list, foundSongs.values.toResponseSongList())
                ),
                songIdList = foundSongs.keys.toList()
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
            }.shuffled(java.util.Random()).take(4)
        )
    }

    override suspend fun getDailyMix(helper: UserTypeHelper) = withContext(Dispatchers.IO) {
        val subResponseOneDeferred = async {
            val historySongIdList = getHistorySongIdList(helper.userType, helper.id)

            val historySongList = historySongIdList.getHistorySongList()

            // get songs based on artists of users last listened songs
            val songsByTheArtistUnSorted = getPreviewSongsByTheArtists(historySongIdList)

            val songByArtistSorted = songsByTheArtistUnSorted.flatMap {
                it.value.take(songsByTheArtistUnSorted.size / 30)
            }.distinctBy {
                it.id
            }.filterNot { songByArtist -> // don't know if they are present so remove first if any
                historySongList.any {
                    it.id == songByArtist.id
                }
            }.shuffled(java.util.Random()).take(30)

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

        DailyMixPreview(
            listOfSongs = (one + two).shuffled(java.util.Random())
        )
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
                GoogleUserListenHistoryTable
                    .slice(
                        EmailUserListenHistoryTable.songId
                    ).select {
                        GoogleUserListenHistoryTable.userId eq userId and (
                                GoogleUserListenHistoryTable.date greaterEq (
                                        LocalDateTime.now().minus(3, ChronoUnit.DAYS)
                                        )
                                )
                    }
                    .withDistinct()
                    .orderBy(SqlRandom())
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
            HomeResponseSong(
                id = it.id.value.toString(),
                title = it.title,
                coverImage = it.coverImage,
                artist = it.artist,
                album = it.album
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
                SongTable.coverImage,
                SongTable.artist,
                SongTable.album,
                SongTable.points
            ).select {
                sar2[SongArtistRelationTable.songId] inList songIdList
            }.orderBy(SongTable.points, SortOrder.DESC)
            .map {
                HomeResponseSong(
                    it[SongTable.id].toString(),
                    it[SongTable.title],
                    it[SongTable.coverImage],
                    it[SongTable.artist],
                    it[SongTable.album]
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
                HomeResponseSong(
                    it[SongTable.id].toString(),
                    it[SongTable.title],
                    it[SongTable.coverImage],
                    it[SongTable.artist],
                    it[SongTable.album]
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
                SongTable.coverImage,
                SongTable.artist,
                SongTable.album,
                SongTable.points
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
                SongTable.coverImage,
                SongTable.artist,
                SongTable.album,
                SongTable.points
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
                SongTable.coverImage,
                SongTable.artist,
                SongTable.album,
                SongTable.points
            ).select {
                PasskeyUserGenreRelationTable.userId eq userId
            }
        }
    }
}