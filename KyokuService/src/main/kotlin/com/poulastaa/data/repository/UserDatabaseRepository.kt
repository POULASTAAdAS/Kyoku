package com.poulastaa.data.repository

import com.poulastaa.data.dao.AlbumDao
import com.poulastaa.data.dao.ArtistDao
import com.poulastaa.data.dao.PlaylistDao
import com.poulastaa.data.dao.SongDao
import com.poulastaa.data.dao.user.EmailAuthUserDao
import com.poulastaa.data.dao.user.GoogleAuthUserDao
import com.poulastaa.data.mappers.toAlbum
import com.poulastaa.data.mappers.toArtistDto
import com.poulastaa.data.mappers.toSongDto
import com.poulastaa.data.mappers.toUserResult
import com.poulastaa.data.model.*
import com.poulastaa.domain.model.PlaylistSongPayload
import com.poulastaa.domain.model.ReqUserPayload
import com.poulastaa.domain.model.UserResult
import com.poulastaa.domain.model.UserType
import com.poulastaa.domain.repository.DatabaseRepository
import com.poulastaa.domain.repository.UserId
import com.poulastaa.domain.repository.UserRepository
import com.poulastaa.domain.table.*
import com.poulastaa.domain.table.relation.*
import com.poulastaa.domain.table.user.EmailAuthUserTable
import com.poulastaa.domain.table.user.GoogleAuthUserTable
import com.poulastaa.plugins.query
import kotlinx.coroutines.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class UserDatabaseRepository(
    private val database: DatabaseRepository,
) : UserRepository {
    override suspend fun getUserOnPayload(payload: ReqUserPayload): UserResult? = when (payload.userType) {
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

    override suspend fun getUserOnEmail(
        email: String,
        userType: UserType,
    ): UserResult? = when (userType) {
        UserType.GOOGLE_USER -> {
            query {
                GoogleAuthUserDao.find {
                    GoogleAuthUserTable.email eq email
                }.singleOrNull()?.toUserResult()
            }
        }

        UserType.EMAIL_USER -> {
            query {
                EmailAuthUserDao.find {
                    EmailAuthUserTable.email eq email
                }.singleOrNull()?.toUserResult()
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
        email: String,
    ): Pair<LogInDto, UserId> = coroutineScope {
        val userId = getUserOnEmail(email, userType)?.id ?: return@coroutineScope LogInDto() to -1

        val savedPlaylistDef = async {
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
                            database.getArtistOnSongId(songDao.id.value) to songDao
                        }
                    }.awaitAll().map { pair ->
                        pair.second.toSongDto(artist = pair.first.joinToString())
                    }
                )
            }
        }
        val savedAlbumDef = async {
            query {
                UserAlbumRelationTable.select {
                    (UserAlbumRelationTable.userId eq userId) and (UserAlbumRelationTable.userType eq userType.name)
                }.map {
                    it[UserAlbumRelationTable.albumId]
                }.let { albumIdList ->
                    query {
                        AlbumDao.find {
                            AlbumTable.id inList albumIdList
                        }.map { album ->
                            async {
                                query {
                                    SongAlbumRelationTable.select {
                                        SongAlbumRelationTable.albumId eq album.id.value
                                    }.limit(1).single().let {
                                        it[SongAlbumRelationTable.songId]
                                    }.let { songId ->
                                        SongDao.find {
                                            SongTable.id eq songId
                                        }.single().constructCoverImage()
                                    }
                                } to album
                            }
                        }.awaitAll()
                    }
                }.map {
                    AlbumDto(
                        id = it.second.id.value,
                        name = it.second.name,
                        coverImage = it.first
                    )
                }
            }
        }
        val followArtistDef = async {
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
        val favouriteSongDef = async { getUserFavouriteSong(userId, userType.name) }

        LogInDto(
            savedPlaylist = savedPlaylistDef.await(),
            savedAlbum = savedAlbumDef.await(),
            savedArtist = followArtistDef.await(),
            favouriteSong = favouriteSongDef.await()
        ) to userId
    }

    override suspend fun addToFavourite(
        id: Long,
        userId: Long,
        userType: UserType,
    ): SongDto = coroutineScope {
        val insert = async {
            if (
                query {
                    SongDao.find {
                        SongTable.id eq id
                    }.singleOrNull() == null
                }
            ) return@async SongDto()

            query {
                UserFavouriteRelationTable.insertIgnore {
                    it[this.songId] = id
                    it[this.userId] = userId
                    it[this.userType] = userType.name
                }
            }
        }

        val song = async {
            val resultRow = query {
                SongArtistRelationTable.select {
                    SongArtistRelationTable.songId eq id
                }
            }

            val artistName = if (query { resultRow.empty() }) ""
            else query {
                resultRow.map {
                    it[SongArtistRelationTable.artistId]
                }.let {
                    ArtistDao.find {
                        ArtistTable.id inList it
                    }.map {
                        it.name
                    }
                }
            }.joinToString()

            query {
                SongDao.find {
                    SongTable.id eq id
                }.singleOrNull()?.toSongDto(artistName)
            } ?: SongDto()
        }

        insert.await()
        song.await()
    }

    override suspend fun removeFromFavourite(
        id: Long,
        email: String,
        userType: UserType,
    ): Boolean {
        val user = getUserOnEmail(email = email, userType = userType) ?: return false

        return query {
            UserFavouriteRelationTable.deleteWhere {
                this.userId eq user.id and
                        (this.userType eq userType.name) and
                        (this.songId eq id)
            }

            true
        }
    }

    override suspend fun addArtist(
        artistId: Long,
        email: String,
        userType: UserType,
    ): ArtistDto {
        val artist = query {
            ArtistDao.find {
                ArtistTable.id eq artistId
            }.singleOrNull()
        } ?: return ArtistDto()

        query {
            UserArtistRelationTable.insertIgnore {
                it[this.artistId] = artistId
                it[this.userType] = userType.name
                it[this.userId] = userId
            }
        }

        return artist.toArtistDto()
    }

    override suspend fun removeArtist(
        artistId: Long,
        userId: Long,
        userType: UserType,
    ): Boolean = query {
        UserArtistRelationTable.deleteWhere {
            this.userId eq userId and
                    (this.userType eq userType.name) and
                    (this.artistId eq artistId)
        }

        true
    }

    override suspend fun addAlbum(
        albumId: Long,
        email: String,
        userType: UserType,
    ): AlbumWithSongDto = coroutineScope {
        val user = getUserOnEmail(email, userType) ?: return@coroutineScope AlbumWithSongDto()

        val album = query {
            AlbumDao.find {
                AlbumTable.id eq albumId
            }.singleOrNull()
        } ?: return@coroutineScope AlbumWithSongDto()

        val insertDef = async {
            query {
                UserAlbumRelationTable.insertIgnore {
                    it[this.albumId] = album.id.value
                    it[this.userType] = userType.name
                    it[this.userId] = user.id
                }
            }
        }
        val songDef = async {
            query {
                SongAlbumRelationTable.select {
                    SongAlbumRelationTable.albumId eq album.id.value
                }.map {
                    it[SongAlbumRelationTable.songId]
                }.let {
                    SongDao.find {
                        SongTable.id inList it
                    }
                }.map {
                    val artist = database.getArtistOnSongId(it.id.value)

                    it.toSongDto(artist.joinToString { resultArtist -> resultArtist.name })
                }
            }
        }

        insertDef.await()
        val songs = songDef.await()

        AlbumWithSongDto(
            albumDto = album.toAlbum(songs.getOrNull(0)?.coverImage ?: ""),
            listOfSong = songs
        )
    }

    override suspend fun removeAlbum(
        id: Long,
        email: String,
        userType: UserType,
    ): Boolean = query {
        UserAlbumRelationTable.deleteWhere {
            this.userId eq userId and
                    (this.userType eq userType.name) and
                    (this.albumId eq id)
        }

        true
    }

    override suspend fun updatePlaylist(
        userId: Long,
        userType: UserType,
        songId: Long,
        map: Map<Long, Boolean>,
    ): Unit = coroutineScope {
        map.map { (playlistId, operation) ->
            async {
                query {
                    when (operation) {
                        true -> UserPlaylistSongRelationTable.insertIgnore {
                            it[this.userId] = userId
                            it[this.playlistId] = playlistId
                            it[this.songId] = songId
                            it[this.userType] = userType.name
                        }

                        false -> UserPlaylistSongRelationTable.deleteWhere {
                            this.userId eq userId and
                                    (this.playlistId eq playlistId) and
                                    (this.songId eq songId) and
                                    (this.userType eq userType.name)
                        }
                    }
                }
            }
        }.awaitAll()
    }

    override suspend fun pinData(
        id: Long,
        userId: Long,
        userType: UserType,
        pinnedType: PinnedType,
    ) {
        query {
            PinnedTable.insertIgnore {
                it[this.id] = id
                it[this.userId] = userId
                it[this.userType] = userType.name
                it[this.pinnedType] = pinnedType.name
            }
        }
    }

    override suspend fun unPinData(
        id: Long,
        userId: Long,
        userType: UserType,
        pinnedType: PinnedType,
    ) {
        query {
            PinnedTable.deleteWhere {
                this.id eq id and
                        (this.userId eq userId) and
                        (this.pinnedType eq pinnedType.name) and
                        (this.userType eq userType.name)
            }
        }
    }

    override suspend fun deleteSavedData(
        id: Long,
        userId: Long,
        userType: UserType,
        dataType: PinnedType, // using PinnedType as saved data type
    ) {
        when (dataType) {
            PinnedType.PLAYLIST -> {
                val playlist = database.getPlaylistOnId(id) ?: return

                query {
                    val userIds = UserPlaylistSongRelationTable
                        .slice(UserPlaylistSongRelationTable.userId)
                        .select {
                            UserPlaylistSongRelationTable.playlistId eq id
                        }.withDistinct()
                        .map { it[UserPlaylistSongRelationTable.userId] }

                    if (userIds.size > 1) UserPlaylistSongRelationTable.deleteWhere {
                        this.userId eq userId and
                                (this.userType eq userType.name) and
                                (this.playlistId eq id)
                    }
                    else playlist.delete()
                }
            }

            PinnedType.ALBUM -> {
                query {
                    UserAlbumRelationTable.deleteWhere {
                        this.userId eq userId and
                                (this.userType eq userType.name) and
                                (this.albumId eq id)
                    }
                }
            }

            PinnedType.ARTIST -> {
                query {
                    UserArtistRelationTable.deleteWhere {
                        this.userId eq userId and
                                (this.userType eq userType.name) and
                                (this.artistId eq id)
                    }
                }
            }

            else -> Unit
        }
    }

    override suspend fun getUserFavouriteSong(
        userId: Long,
        userType: String,
    ): List<SongDto> = coroutineScope {
        query {
            UserFavouriteRelationTable.select {
                UserFavouriteRelationTable.userId eq userId and (UserFavouriteRelationTable.userType eq userType)
            }.map {
                it[UserFavouriteRelationTable.songId]
            }.let {
                SongDao.find {
                    SongTable.id inList it
                }.map { songDao ->
                    async {
                        database.getArtistOnSongId(songDao.id.value) to songDao
                    }
                }.awaitAll().map {
                    it.second.toSongDto(it.first.joinToString { artist -> artist.name })
                }
            }
        }
    }

    override suspend fun getSyncAlbum(
        userId: Long,
        userType: UserType,
        albumIdList: List<Long>,
    ): SyncDto<Any> = coroutineScope {
        val savedAlbumIdList = query {
            UserAlbumRelationTable
                .select {
                    UserAlbumRelationTable.userId eq userId and
                            (UserAlbumRelationTable.userType eq userType.name)
                }.map {
                    it[UserAlbumRelationTable.albumId]
                }
        }

        val removeList = albumIdList.filterNot { savedAlbumIdList.contains(it) }

        val newList = savedAlbumIdList.filterNot { albumIdList.contains(it) }
            .map {
                async {
                    val album = async { database.getAlbumOnId(it)!! }
                    val song = async { database.getAlbumSong(it) }

                    album.await() to song.await()
                }
            }.awaitAll()

        SyncDto(
            removeIdList = removeList,
            newAlbumList = newList.map {
                AlbumWithSongDto(
                    albumDto = AlbumDto(
                        id = it.first.id.value,
                        name = it.first.name,
                        coverImage = it.second.first().coverImage.ifEmpty { "" }
                    ),
                    listOfSong = it.second
                )
            }
        )
    }

    override suspend fun getSyncPlaylist(
        userId: Long,
        userType: UserType,
        playlistIdList: List<Long>,
    ): SyncDto<Any> = coroutineScope {
        val savedPlaylistIdList = query {
            UserPlaylistSongRelationTable
                .select {
                    UserPlaylistSongRelationTable.userId eq userId and
                            (UserPlaylistSongRelationTable.userType eq userType.name)
                }.map {
                    it[UserPlaylistSongRelationTable.playlistId]
                }
        }

        val removeList = playlistIdList.filterNot { savedPlaylistIdList.contains(it) }

        val newList = savedPlaylistIdList.filterNot { playlistIdList.contains(it) }
            .map {
                async {
                    val playlist = async { database.getPlaylistOnId(it)!! }
                    val song = async { database.getPlaylistSong(it, userId, userType) }

                    playlist.await() to song.await()
                }
            }.awaitAll()

        SyncDto(
            removeIdList = removeList,
            newAlbumList = newList.map {
                PlaylistDto(
                    id = it.first.id.value,
                    name = it.first.name,
                    listOfSong = it.second
                )
            }
        )
    }

    override suspend fun getSyncArtist(
        userId: Long,
        userType: UserType,
        artistIdList: List<Long>,
    ): SyncDto<Any> = coroutineScope {
        val savedArtistIdList = query {
            UserArtistRelationTable
                .select {
                    UserArtistRelationTable.userId eq userId and
                            (UserArtistRelationTable.userType eq userType.name)
                }.map {
                    it[UserArtistRelationTable.artistId]
                }
        }

        val removeList = artistIdList.filterNot { savedArtistIdList.contains(it) }

        val newList = savedArtistIdList.filterNot { artistIdList.contains(it) }
            .map {
                async { database.getArtistOnId(it)!!.toArtistDto() }
            }.awaitAll()

        SyncDto(
            removeIdList = removeList,
            newAlbumList = newList
        )
    }
}
