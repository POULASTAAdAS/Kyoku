package com.poulastaa.domain.repository

import com.poulastaa.data.model.ArtistDto
import com.poulastaa.data.model.home.PreArtistSongDto
import com.poulastaa.data.model.home.PrevAlbumDto
import com.poulastaa.data.model.home.PrevSongDto
import com.poulastaa.domain.model.DayType
import com.poulastaa.domain.model.UserType

interface HomeRepository {
    suspend fun getPopularSongMixPrev(
        countryId: Int,
    ): List<PrevSongDto>

    suspend fun getPopularSongFromUserTimePrev(
        year: Int,
        countryId: Int,
    ): List<PrevSongDto>

    suspend fun getFavouriteArtistMixPrev(
        userId: Long,
        userType: UserType,
        countryId: Int,
    ): List<PrevSongDto>

    suspend fun getDayTypeSongPrev(
        dayType: DayType,
        countryId: Int,
    ): List<PrevSongDto>

    suspend fun getPopularAlbumPrev(
        countryId: Int,
    ): List<PrevAlbumDto>

    suspend fun getPopularArtist(
        userId: Long,
        userType: UserType,
        countryId: Int,
    ): List<ArtistDto>

    suspend fun getPopularArtistSongPrev(
        userId: Long,
        userType: UserType,
        excludeArtist: List<Long>,
        countryId: Int,
    ): List<PreArtistSongDto>
}