package com.poulastaa.kyoku.shardmanager.app.core.domain.repository

import com.poulastaa.kyoku.shardmanager.app.core.database.model.DtoGenreArtistRelation
import com.poulastaa.kyoku.shardmanager.app.core.domain.model.DtoDBArtist

typealias Genre = String

interface LocalShardUpdateDatasource {
    suspend fun isGenreArtistShardDatabasePopulated(): Boolean
    suspend fun isSuggestionShardDatabasePopulated(): Boolean

    // database genre artist shard
    suspend fun getAllArtist(): List<DtoDBArtist>
    suspend fun getShardGenreArtistRelation(): List<DtoGenreArtistRelation>

    suspend fun createShardArtistTable()
    suspend fun insertIntoShardArtist(data: List<DtoDBArtist>)
    suspend fun insertIntoShardGenreArtistRelation(data: Map<Genre, List<DtoGenreArtistRelation>>)

    suspend fun upsertSongPopularity()
    suspend fun upsertArtistPopularity()

    suspend fun updateShardGenreArtistsRelation()

    // database suggestion shard
    suspend fun getSongs(): List<Pair<Long, Long>>
    suspend fun insertShardSongs(data: List<Pair<Long, Long>>)

    suspend fun insertCountrysMostPopularSongs()
    suspend fun insertYearMostPopularSongs()
    suspend fun insertArtistsMostPopularSongs()

    suspend fun updateCountryMostPopularSongs()
    suspend fun updateYearMostPopularSongs()
    suspend fun updateArtistMostPopularSongs()
}