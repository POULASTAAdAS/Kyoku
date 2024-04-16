package com.poulastaa.data.repository.artist

import com.poulastaa.data.model.artist.ViewArtist
import com.poulastaa.data.model.common.ResponseArtist
import com.poulastaa.data.model.common.ResponseSong
import com.poulastaa.data.model.db_table.ArtistTable
import com.poulastaa.data.model.db_table.SongArtistRelationTable
import com.poulastaa.data.model.db_table.SongTable
import com.poulastaa.data.model.db_table.user_artist.EmailUserArtistRelationTable
import com.poulastaa.data.model.db_table.user_artist.GoogleUserArtistRelationTable
import com.poulastaa.data.model.db_table.user_artist.PasskeyUserArtistRelationTable
import com.poulastaa.data.model.db_table.user_listen_history.EmailUserListenHistoryTable
import com.poulastaa.data.model.db_table.user_listen_history.GoogleUserListenHistoryTable
import com.poulastaa.data.model.db_table.user_listen_history.PasskeyUserListenHistoryTable
import com.poulastaa.data.model.db_table.user_pinned_artist.EmailUserPinnedArtistTable
import com.poulastaa.data.model.db_table.user_pinned_artist.GoogleUserPinnedArtistTable
import com.poulastaa.data.model.db_table.user_pinned_artist.PasskeyUserPinnedArtistTable
import com.poulastaa.data.model.home.ArtistSong
import com.poulastaa.data.model.home.FevArtistsMixPreview
import com.poulastaa.data.model.home.ResponseArtistsPreview
import com.poulastaa.data.model.item.ItemOperation
import com.poulastaa.data.model.setup.artist.ArtistResponseStatus
import com.poulastaa.data.model.setup.artist.StoreArtistResponse
import com.poulastaa.data.model.setup.artist.SuggestArtistReq
import com.poulastaa.data.model.setup.artist.SuggestArtistResponse
import com.poulastaa.data.model.utils.DbResponseArtistPreview
import com.poulastaa.data.model.utils.UserType
import com.poulastaa.domain.dao.Artist
import com.poulastaa.domain.dao.Song
import com.poulastaa.domain.dao.user_artist.EmailUserArtistRelation
import com.poulastaa.domain.dao.user_artist.GoogleUserArtistRelation
import com.poulastaa.domain.dao.user_artist.PasskeyUserArtistRelation
import com.poulastaa.domain.repository.aritst.ArtistRepository
import com.poulastaa.plugins.dbQuery
import com.poulastaa.utils.constructCoverPhotoUrl
import com.poulastaa.utils.constructMasterPlaylistUrl
import com.poulastaa.utils.toResponseArtist
import com.poulastaa.utils.toViewArtist
import kotlinx.coroutines.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import kotlin.random.Random

class ArtistRepositoryImpl : ArtistRepository {
    override suspend fun suggestArtist(
        req: SuggestArtistReq,
        countryId: Int
    ): SuggestArtistResponse {
        val artistList = dbQuery {
            Artist.find {  // sort by points
                ArtistTable.country eq countryId
            }.orderBy(ArtistTable.points to SortOrder.DESC)
                .toResponseArtist()
                .removeDuplicate(req.alreadySendArtistList, req.isSelected)
        }

        return SuggestArtistResponse(
            status = ArtistResponseStatus.SUCCESS,
            artistList = artistList
        )
    }

    override suspend fun storeArtist(
        usedId: Long,
        userType: UserType,
        artistNameList: List<String>
    ): StoreArtistResponse {
        val artistIdList = dbQuery {
            Artist.find {
                ArtistTable.name inList artistNameList
            }.map {
                it.id.value
            }
        }

        when (userType) {
            UserType.GOOGLE_USER -> artistIdList.storeArtistForGoogleUser(id = usedId)

            UserType.EMAIL_USER -> artistIdList.storeArtistForEmailUser(id = usedId)

            UserType.PASSKEY_USER -> artistIdList.storeArtistForPasskeyUser(id = usedId)
        }

        incrementArtistPoints(artistIdList)

        return StoreArtistResponse(
            status = ArtistResponseStatus.SUCCESS
        )
    }

    override suspend fun getFevArtistMixPreview(
        usedId: Long,
        userType: UserType
    ) = when (userType) {
        UserType.GOOGLE_USER -> {
            dbQuery {
                GoogleUserArtistRelation.find {
                    GoogleUserArtistRelationTable.userId eq usedId
                }.map {
                    it.artistId
                }.getListOfFevArtistMixPreview()
            }
        }

        UserType.EMAIL_USER -> {
            dbQuery {
                EmailUserArtistRelation.find {
                    EmailUserArtistRelationTable.userId eq usedId
                }.map {
                    it.artistId
                }.getListOfFevArtistMixPreview()
            }
        }

        UserType.PASSKEY_USER -> {
            dbQuery {
                PasskeyUserArtistRelation.find {
                    PasskeyUserArtistRelationTable.userId eq usedId
                }.map {
                    it.artistId
                }.getListOfFevArtistMixPreview()
            }
        }
    }

    override suspend fun getResponseArtistPreviewForNewUser(
        usedId: Long,
        userType: UserType
    ) = dbQuery {
        getResponseArtistOnUserId(usedId, userType)
            .orderBy(
                column = ArtistTable.points,
                order = SortOrder.DESC
            ).orderBy(
                column = SongTable.points,
                order = SortOrder.DESC
            ).mapToResponseArtistsPreview()
    }


    override suspend fun getResponseArtistPreviewDailyUser(
        usedId: Long,
        userType: UserType
    ): List<ResponseArtistsPreview> = withContext(Dispatchers.IO) {
        val historyArtistIdListDeferred = async { getHistoryArtistIdList(userType, usedId) }
        val fevArtistIdListDeferred = async { getFevArtistIdList(userType, usedId) }

        val historyArtistIdList = historyArtistIdListDeferred.await()
        val fevArtistIdList = fevArtistIdListDeferred.await().filterNot {
            it in historyArtistIdList
        }

        (historyArtistIdList + fevArtistIdList.take(3)).getResponseArtistPreviewOnArtistIdList()
    }

    private fun Iterable<ResponseArtist>.removeDuplicate(
        oldList: List<String>,
        isSelectedReq: Boolean
    ): List<ResponseArtist> {
        val filteredResult = this.filterNot {
            oldList.contains(it.name)
        }

        return filteredResult.take(if (isSelectedReq) 3 else 5)
    }


    override suspend fun getArtistMix(userType: UserType, userId: Long): List<ResponseSong> {
        val favArtist = getFavArtist(userType, userId)

        val original = favArtist.getOriginal()
        val all = favArtist.getAll().filterNot {
            original.map { preview -> preview.id }.contains(it.id)
        }

        return (original + all).shuffled(
            Random(seed = original.size + all.size)
        ).take(Random.nextInt(50, 60))
    }

    override suspend fun handleArtist(
        userId: Long,
        userType: UserType,
        artistId: Long,
        operation: ItemOperation
    ): Boolean = withContext(Dispatchers.IO) {
        when (operation) {
            ItemOperation.ADD -> addArtist(userId = userId, artistId = artistId.toInt(), userType = userType)

            ItemOperation.DELETE -> {
                async { deleteUserArtist(userId = userId, artistId = artistId.toInt(), userType = userType) }.await()
                async { deletePinnedArtist(userId = userId, artistId = artistId.toInt(), userType = userType) }.await()
            }

            ItemOperation.ERR -> false
        }
    }

    override suspend fun getResponseArtistOnSongId(songId: Long): List<ViewArtist> = withContext(Dispatchers.IO) {
        dbQuery {
            val artistIdList = SongArtistRelationTable.slice(
                SongArtistRelationTable.artistId
            ).select {
                SongArtistRelationTable.songId eq songId
            }.map {
                it[SongArtistRelationTable.artistId]
            }

            Artist.find {
                ArtistTable.id inList artistIdList
            }.toViewArtist()
        }
    }

    private suspend fun List<Int>.storeArtistForEmailUser(id: Long) {
        dbQuery {
            this.forEach {
                val found = EmailUserArtistRelation.find {
                    EmailUserArtistRelationTable.userId eq id and (EmailUserArtistRelationTable.artistId eq it)
                }.firstOrNull()

                if (found == null) {
                    EmailUserArtistRelation.new {
                        this.artistId = it
                        this.userId = id
                    }
                }
            }
        }
    }

    private suspend fun List<Int>.storeArtistForGoogleUser(id: Long) {
        dbQuery {
            this.forEach {
                val found = GoogleUserArtistRelation.find {
                    GoogleUserArtistRelationTable.userId eq id and (GoogleUserArtistRelationTable.artistId eq it)
                }.firstOrNull()

                if (found == null) {
                    GoogleUserArtistRelation.new {
                        this.artistId = it
                        this.userId = id
                    }
                }
            }
        }
    }

    private suspend fun List<Int>.storeArtistForPasskeyUser(id: Long) {
        dbQuery {
            this.forEach {
                val found = PasskeyUserArtistRelation.find {
                    PasskeyUserArtistRelationTable.userId eq id and (PasskeyUserArtistRelationTable.artistId eq it)
                }.firstOrNull()

                if (found == null) {
                    PasskeyUserArtistRelation.new {
                        this.artistId = it
                        this.userId = id
                    }
                }
            }
        }
    }


    private fun incrementArtistPoints(idList: List<Int>) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = dbQuery {
                idList.mapNotNull {
                    Artist.find {
                        ArtistTable.id eq it
                    }.firstOrNull()
                }
            }

            result.forEach {
                dbQuery {
                    it.points = ++it.points
                }
            }
        }
    }

    private suspend fun List<Int>.getListOfFevArtistMixPreview() = dbQuery {
        Song.find {
            SongTable.id inList SongArtistRelationTable.slice(
                SongArtistRelationTable.songId
            ).select {
                SongArtistRelationTable.artistId inList this@getListOfFevArtistMixPreview
            }.map {
                it[SongArtistRelationTable.songId]
            }
        }.orderBy(SongTable.points to SortOrder.DESC)
            .limit(4).map {
                FevArtistsMixPreview(
                    coverImage = it.coverImage.constructCoverPhotoUrl(),
                    artist = it.artist
                )
            }
    }

    private fun getResponseArtistOnUserId(
        usedId: Long,
        userType: UserType
    ) = when (userType) {
        UserType.GOOGLE_USER -> {
            SongTable
                .join(
                    otherTable = ArtistTable,
                    joinType = JoinType.INNER,
                    additionalConstraint = {
                        SongTable.artist eq ArtistTable.name
                    }
                ).join(
                    otherTable = GoogleUserArtistRelationTable,
                    joinType = JoinType.INNER,
                    additionalConstraint = {
                        ArtistTable.id eq GoogleUserArtistRelationTable.artistId as Column<*>
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
                    GoogleUserArtistRelationTable.userId eq usedId
                }
        }

        UserType.EMAIL_USER -> {
            SongTable
                .join(
                    otherTable = ArtistTable,
                    joinType = JoinType.INNER,
                    additionalConstraint = {
                        SongTable.artist eq ArtistTable.name
                    }
                ).join(
                    otherTable = EmailUserArtistRelationTable,
                    joinType = JoinType.INNER,
                    additionalConstraint = {
                        ArtistTable.id eq EmailUserArtistRelationTable.artistId as Column<*>
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
                    EmailUserArtistRelationTable.userId eq usedId
                }
        }

        UserType.PASSKEY_USER -> {
            SongTable
                .join(
                    otherTable = ArtistTable,
                    joinType = JoinType.INNER,
                    additionalConstraint = {
                        SongTable.artist eq ArtistTable.name
                    }
                ).join(
                    otherTable = PasskeyUserArtistRelationTable,
                    joinType = JoinType.INNER,
                    additionalConstraint = {
                        ArtistTable.id eq PasskeyUserArtistRelationTable.artistId as Column<*>
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
                    PasskeyUserArtistRelationTable.userId eq usedId
                }
        }
    }

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
                SongArtistRelationTable.slice(
                    SongArtistRelationTable.artistId
                ).select {
                    SongArtistRelationTable.songId inList it
                }.map {
                    it[SongArtistRelationTable.artistId]
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
    }.groupBy { it.artist }.map {
        ResponseArtistsPreview(
            artist = ResponseArtist(
                id = it.value[0].artistId.toLong(),
                name = it.key,
                imageUrl = ResponseArtist.getArtistImageUrl(it.value[0].artistImage)
            ),
            listOfSongs = it.value.map { result ->
                ArtistSong(
                    songId = result.songId.toLong(),
                    title = result.songTitle,
                    coverImage = result.songCover
                )
            }.take(6)
        )
    }

    private suspend fun getFavArtist(userType: UserType, userId: Long) = withContext(Dispatchers.IO) {
        dbQuery {
            when (userType) {
                UserType.GOOGLE_USER -> {
                    ArtistTable
                        .join(
                            otherTable = GoogleUserArtistRelationTable,
                            joinType = JoinType.INNER,
                            additionalConstraint = {
                                GoogleUserArtistRelationTable.artistId as Column<*> eq ArtistTable.id
                            }
                        ).select {
                            GoogleUserArtistRelationTable.userId eq userId
                        }.map {
                            it[ArtistTable.name]
                        }
                }

                UserType.EMAIL_USER -> {
                    ArtistTable
                        .join(
                            otherTable = EmailUserArtistRelationTable,
                            joinType = JoinType.INNER,
                            additionalConstraint = {
                                EmailUserArtistRelationTable.artistId as Column<*> eq ArtistTable.id
                            }
                        ).select {
                            EmailUserArtistRelationTable.userId eq userId
                        }.map {
                            it[ArtistTable.name]
                        }
                }

                UserType.PASSKEY_USER -> {
                    ArtistTable
                        .join(
                            otherTable = PasskeyUserArtistRelationTable,
                            joinType = JoinType.INNER,
                            additionalConstraint = {
                                PasskeyUserArtistRelationTable.artistId as Column<*> eq ArtistTable.id
                            }
                        ).select {
                            PasskeyUserArtistRelationTable.userId eq userId
                        }.map {
                            it[ArtistTable.name]
                        }
                }
            }
        }
    }

    private suspend fun List<String>.getOriginal() = withContext(Dispatchers.IO) {
        dbQuery {
            Song.find {
                SongTable.artist inList this@getOriginal
            }.orderBy(org.jetbrains.exposed.sql.Random() to SortOrder.ASC)
                .map {
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
                }.groupBy { it.artist }
                .map {
                    it.value.take(35)
                }.flatten()
        }
    }

    private suspend fun List<String>.getAll() = dbQuery {
        this.map {
            Song.find {
                SongTable.artist like "%$it%"
            }.orderBy(SongTable.points to SortOrder.DESC)
                .map {
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
                }.take(50).shuffled(Random)
        }.flatten()
    }


    private suspend fun addArtist(
        userId: Long,
        artistId: Int,
        userType: UserType
    ) = dbQuery {
        when (userType) {
            UserType.GOOGLE_USER -> {
                val response = GoogleUserArtistRelation.find {
                    GoogleUserArtistRelationTable.artistId eq artistId and
                            (GoogleUserArtistRelationTable.userId eq userId)
                }.firstOrNull()

                if (response == null)
                    GoogleUserArtistRelation.new {
                        this.artistId = artistId
                        this.userId = userId
                    }
            }

            UserType.EMAIL_USER -> {
                val response = EmailUserArtistRelation.find {
                    EmailUserArtistRelationTable.artistId eq artistId and
                            (EmailUserArtistRelationTable.userId eq userId)
                }.firstOrNull()

                if (response == null)
                    EmailUserArtistRelation.new {
                        this.artistId = artistId
                        this.userId = userId
                    }
            }

            UserType.PASSKEY_USER -> {
                val response = PasskeyUserArtistRelation.find {
                    PasskeyUserArtistRelationTable.artistId eq artistId and
                            (PasskeyUserArtistRelationTable.userId eq userId)
                }.firstOrNull()

                if (response == null)
                    PasskeyUserArtistRelation.new {
                        this.artistId = artistId
                        this.userId = userId
                    }
            }
        }
    }.let { true }

    private suspend fun deleteUserArtist(
        userId: Long,
        artistId: Int,
        userType: UserType
    ) = dbQuery {
        when (userType) {
            UserType.GOOGLE_USER -> {
                GoogleUserArtistRelation.find {
                    GoogleUserArtistRelationTable.userId eq userId and
                            (GoogleUserArtistRelationTable.artistId eq artistId)
                }.firstOrNull()?.delete()
            }

            UserType.EMAIL_USER -> {
                EmailUserArtistRelation.find {
                    EmailUserArtistRelationTable.userId eq userId and
                            (EmailUserArtistRelationTable.artistId eq artistId)
                }.firstOrNull()?.delete()
            }

            UserType.PASSKEY_USER -> {
                PasskeyUserArtistRelation.find {
                    PasskeyUserArtistRelationTable.userId eq userId and
                            (PasskeyUserArtistRelationTable.artistId eq artistId)
                }.firstOrNull()?.delete()
            }
        }
    }.let { true }

    private suspend fun deletePinnedArtist(
        userId: Long,
        artistId: Int,
        userType: UserType
    ) = dbQuery {
        when (userType) {
            UserType.GOOGLE_USER -> {
                GoogleUserPinnedArtistTable.deleteWhere {
                    GoogleUserPinnedArtistTable.artistId eq artistId and
                            (GoogleUserPinnedArtistTable.userId eq userId)
                }
            }

            UserType.EMAIL_USER -> {
                EmailUserPinnedArtistTable.deleteWhere {
                    EmailUserPinnedArtistTable.artistId eq artistId and
                            (EmailUserPinnedArtistTable.userId eq userId)
                }
            }

            UserType.PASSKEY_USER -> {
                PasskeyUserPinnedArtistTable.deleteWhere {
                    PasskeyUserPinnedArtistTable.artistId eq artistId and
                            (PasskeyUserPinnedArtistTable.userId eq userId)
                }
            }
        }
    }.let { true }
}