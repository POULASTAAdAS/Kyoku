package com.poulastaa.kyoku.shardmanager.app.core.domain.repository

interface LocalShardUpdateDatasource {
    suspend fun updateSongPopularity()
    suspend fun updateArtistPopularity()

    suspend fun updateGenreMostPopularArtists()

    suspend fun updateArtistMostPopularSongs()
    suspend fun updateCountryMostPopularSongs()
    suspend fun updateYearMostPopularSongs()
}