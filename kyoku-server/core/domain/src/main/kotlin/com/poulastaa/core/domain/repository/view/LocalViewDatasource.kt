package com.poulastaa.core.domain.repository.view

import com.poulastaa.core.domain.model.*
import com.poulastaa.core.domain.repository.*

interface LocalViewDatasource {
    suspend fun getUserByEmail(email: String, userType: UserType): DtoDBUser?

    suspend fun getArtist(artistId: ArtistId): DtoPrevArtist?
    suspend fun getArtistMostPopularSongs(artistId: ArtistId): List<DtoDetailedPrevSong>

    suspend fun getPrevFullPlaylist(playlistId: PlaylistId): DtoViewOtherPayload<DtoSong>?
    suspend fun getPrevFullAlbum(albumId: AlbumId): DtoViewOtherPayload<DtoDetailedPrevSong>?
    suspend fun getPrevFev(userId: Long): DtoViewOtherPayload<DtoSong>?
    suspend fun getPopularSongMix(userId: Long, songIds: List<SongId>): DtoViewOtherPayload<DtoSong>?
    suspend fun getPopularYearMix(
        userId: Long,
        birthYear: Int,
        countryId: CountryId,
        songIds: List<SongId>,
    ): DtoViewOtherPayload<DtoSong>?

    suspend fun getPopularArtistSongMix(userId: Long, songIds: List<SongId>): DtoViewOtherPayload<DtoSong>?
    suspend fun getPopularDayTimeMix(userId: Long, songIds: List<SongId>): DtoViewOtherPayload<DtoSong>?
}