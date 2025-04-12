package com.poulastaa.core.domain.repository.suggestion

import com.poulastaa.core.domain.model.*
import com.poulastaa.core.domain.repository.*

interface LocalSuggestionDatasource {
    suspend fun getUserByEmail(email: String, userType: UserType): DtoDBUser?

    suspend fun getPrevPopularCountrySong(
        userId: Long,
        countryId: CountryId,
        oldList: List<SongId> = emptyList(),
    ): List<DtoPrevSong>

    suspend fun getPrevPopularArtistMix(
        userId: Long,
        oldList: List<SongId> = emptyList(),
    ): List<DtoPrevSong>

    suspend fun getPrevPopularYearSongs(
        userId: Long,
        birthYear: Int,
        oldList: List<SongId> = emptyList(),
    ): List<DtoPrevSong>

    suspend fun getSuggestedArtist(
        userId: Long,
        countryId: CountryId,
        oldList: List<ArtistId> = emptyList(),
    ): List<DtoPrevArtist>

    suspend fun getSuggestedAlbum(
        userId: Long,
        oldList: List<AlbumId> = emptyList(),
    ): List<DtoAlbum>

    suspend fun getSuggestedArtistSong(
        userId: Long,
        suggestedArtistIdList: List<ArtistId>,
        oldList: List<ArtistId> = emptyList(),
    ): List<DtoSuggestedArtistSong>

    suspend fun getSavedPlaylist(userId: Long): List<DtoFullPlaylist>
    suspend fun getSavedAlbum(userId: Long): List<DtoFullAlbum>
    suspend fun getSavedArtist(userId: Long): List<DtoArtist>

    suspend fun getYourFavouriteSongToAddToPlaylist(userId: UserId): List<DtoDetailedPrevSong>
    suspend fun getSuggestedSongToAddToPlaylist(): List<DtoDetailedPrevSong>
    suspend fun getYouMayAlsoLikeSongToAddToPlaylist(countryId: CountryId): List<DtoDetailedPrevSong>
}