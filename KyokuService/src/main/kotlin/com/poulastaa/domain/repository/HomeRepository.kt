package com.poulastaa.domain.repository

import com.poulastaa.data.model.ArtistDto
import com.poulastaa.data.model.home.PreArtistSongDto
import com.poulastaa.data.model.home.PrevAlbumDto
import com.poulastaa.data.model.home.PrevSongDto
import com.poulastaa.domain.model.DayType
import com.poulastaa.domain.model.UserType

interface HomeRepository {
    suspend fun getPopularSongMix(
        countryId: Int,
    ): List<PrevSongDto>

    suspend fun getPopularSongFromUserTime(
        year: Int,
        countryId: Int,
    ): List<PrevSongDto>

    suspend fun getFavouriteArtistMix(
        userId: Long,
        userType: UserType,
        countryId: Int,
    ): List<PrevSongDto>

    suspend fun getDayTypeSong(
        dayType: DayType,
        countryId: Int,
    ): List<PrevSongDto>

    suspend fun getPopularAlbum(
        countryId: Int,
    ): List<PrevAlbumDto>

    suspend fun getPopularArtist(
        userId: Long,
        userType: UserType,
        countryId: Int,
    ): List<ArtistDto>

    suspend fun getPopularArtistSong(
        userId: Long,
        userType: UserType,
        excludeArtist: List<Long>,
        countryId: Int,
    ): List<PreArtistSongDto>
}