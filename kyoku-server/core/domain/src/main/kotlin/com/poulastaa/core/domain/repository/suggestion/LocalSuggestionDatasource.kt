package com.poulastaa.core.domain.repository.suggestion

import com.poulastaa.core.domain.model.*
import com.poulastaa.core.domain.repository.AlbumId
import com.poulastaa.core.domain.repository.ArtistId
import com.poulastaa.core.domain.repository.CountryId
import com.poulastaa.core.domain.repository.SongId

interface LocalSuggestionDatasource {
    suspend fun getUserByEmail(email: String, userType: UserType): DtoDBUser?

    suspend fun getPrevPopularSongMix(
        userId: Long,
        countryId: CountryId,
        oldList: List<SongId> = emptyList(),
    ): List<DtoPrevSong>

    suspend fun getPrevPopularArtistMix(
        userId: Long,
        countryId: CountryId,
        oldList: List<SongId> = emptyList(),
    ): List<DtoPrevSong>

    suspend fun getPrevOldGem(
        userId: Long,
        countryId: CountryId,
        oldList: List<SongId> = emptyList(),
    ): List<DtoPrevSong>

    suspend fun getSuggestedArtist(userId: Long, oldList: List<ArtistId> = emptyList()): List<DtoPrevArtist>
    suspend fun getSuggestedAlbum(userId: Long, oldList: List<AlbumId> = emptyList()): List<DtoPrevAlbum>
    suspend fun getSuggestedArtistSong(
        userId: Long,
        oldList: List<ArtistId> = emptyList(),
    ): List<DtoSuggestedArtistSong>

    suspend fun getSavedPlaylist(userId: Long): List<DtoFullPlaylist>
    suspend fun getSavedAlbum(userId: Long): List<DtoFullAlbum>
    suspend fun getSavedArtist(userId: Long): List<DtoArtist>
}