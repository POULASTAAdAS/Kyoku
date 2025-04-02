package com.poulastaa.kyoku.shardmanager.app.core.database.utils

import com.poulastaa.kyoku.shardmanager.app.core.domain.repository.LocalShardUpdateDatasource
import com.poulastaa.kyoku.shardmanager.app.core.domain.utils.CURRENT_TIME
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking

fun populateSuggestionShardTables(db: LocalShardUpdateDatasource) = runBlocking {
    if (db.isSuggestionShardDatabasePopulated()) return@runBlocking println("$CURRENT_TIME Suggestion shard tables already populated")

    coroutineScope {
        // insert song
        db.getSongIdWithPopularity().chunked(10000).map {
            println("$CURRENT_TIME Inserting ${it.size} songs")
            db.insertShardSongs(it)
        }

        listOf(
            async { db.insertCountrysMostPopularSongs() },
            async { db.insertArtistsMostPopularSongs() },
            async { db.insertYearMostPopularSongs() }
        ).awaitAll()
    }
}



