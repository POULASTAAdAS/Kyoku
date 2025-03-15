package com.poulastaa.core.domain.repository.view

import com.poulastaa.core.domain.model.*
import com.poulastaa.core.domain.repository.*

interface LocalViewDatasource {
    suspend fun getUserByEmail(email: String, userType: UserType): DtoDBUser?

    suspend fun getArtist(artistId: ArtistId): DtoPrevArtist?
    suspend fun getArtistMostPopularSongs(artistId: ArtistId): List<DtoDetailedPrevSong>

    suspend fun getPrevFullPlaylist(playlistId: PlaylistId): DtoViewOtherPayload?
    suspend fun getPrevFullAlbum(albumId: AlbumId): DtoViewOtherPayload?
    suspend fun getPrevFev(userId: Long): DtoViewOtherPayload?
    suspend fun getPopularSongMix(userId: Long, songIds: List<SongId>): DtoViewOtherPayload?
    suspend fun getPopularYearMix(
        userId: Long,
        birthYear: Int,
        countryId: CountryId,
        songIds: List<SongId>,
    ): DtoViewOtherPayload?

    suspend fun getPopularArtistSongMix(userId: Long, songIds: List<SongId>): DtoViewOtherPayload?
    suspend fun getPopularDayTimeMix(userId: Long, songIds: List<SongId>): DtoViewOtherPayload?
}