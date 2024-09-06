package com.poulastaa.domain.repository

import com.poulastaa.data.model.*
import com.poulastaa.domain.model.ReqUserPayload
import com.poulastaa.domain.model.UserResult
import com.poulastaa.domain.model.UserType

typealias UserId = Long

interface UserRepository {
    suspend fun getUserOnPayload(payload: ReqUserPayload): UserResult?
    suspend fun getUserOnEmail(
        email: String,
        userType: UserType,
    ): UserResult?

    fun createUserPlaylist(
        userId: Long,
        userType: UserType,
        playlistId: Long,
        songIdList: List<Long>,
    )

    suspend fun updateBDate(
        userId: Long,
        bDate: Long,
        userType: UserType,
    ): Boolean

    suspend fun storeGenre(
        userId: Long,
        userType: UserType,
        idList: List<Int>,
    )

    suspend fun storeArtist(
        userId: Long,
        userType: UserType,
        idList: List<Long>,
    )

    suspend fun getUserData(
        userType: UserType,
        email: String,
    ): Pair<LogInDto, UserId>

    suspend fun addToFavourite(
        id: Long,
        userId: Long,
        userType: UserType,
    ): SongDto

    suspend fun removeFromFavourite(
        id: Long,
        email: String,
        userType: UserType,
    ): Boolean

    suspend fun addArtist(
        list: List<Long>,
        userId: Long,
        userType: UserType,
    ): List<ArtistDto>

    suspend fun removeArtist(
        artistId: Long,
        userId: Long,
        userType: UserType,
    ): Boolean

    suspend fun addAlbum(
        list: List<Long>,
        email: String,
        userType: UserType,
    ): List<AlbumWithSongDto>

    suspend fun removeAlbum(
        id: Long,
        email: String,
        userType: UserType,
    ): Boolean

    suspend fun updatePlaylist(
        userId: Long,
        userType: UserType,
        songId: Long,
        map: Map<Long, Boolean>,
    )

    suspend fun pinData(
        id: Long,
        userId: Long,
        userType: UserType,
        pinnedType: PinnedType,
    )

    suspend fun unPinData(
        id: Long,
        userId: Long,
        userType: UserType,
        pinnedType: PinnedType,
    )

    suspend fun deleteSavedData(
        id: Long,
        userId: Long,
        userType: UserType,
        dataType: PinnedType,
    )

    suspend fun getUserFavouriteSong(
        userId: Long,
        userType: String,
    ): List<SongDto>

    suspend fun getSyncAlbum(
        userId: Long,
        userType: UserType,
        albumIdList: List<Long>,
    ): SyncDto<Any>

    suspend fun getSyncPlaylist(
        userId: Long,
        userType: UserType,
        playlistIdList: List<Long>,
    ): SyncDto<Any>

    suspend fun getSyncArtist(
        userId: Long,
        userType: UserType,
        artistIdList: List<Long>,
    ): SyncDto<Any>
}