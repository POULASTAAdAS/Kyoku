package com.poulastaa.kyoku.shardmanager.app.core.database.utils

import com.poulastaa.kyoku.shardmanager.app.core.domain.repository.LocalShardUpdateDatasource
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking

fun createSuggestionShardTables(db: LocalShardUpdateDatasource) = runBlocking {
    if (db.isSuggestionShardDatabasePopulated()) return@runBlocking println("Suggestion shard tables already populated")

    coroutineScope {
        // insert song
        db.getSongs().chunked(10000).map {
            println("Inserting ${it.size} songs")
            db.insertShardSongs(it)
        }

        listOf(
            async { db.insertCountrysMostPopularSongs() },
            async { db.insertArtistsMostPopularSongs() },
            async { db.insertYearMostPopularSongs() }
        ).awaitAll()
    }
}



