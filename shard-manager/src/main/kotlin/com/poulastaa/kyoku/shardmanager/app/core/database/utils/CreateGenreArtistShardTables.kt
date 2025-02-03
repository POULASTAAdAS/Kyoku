package com.poulastaa.kyoku.shardmanager.app.core.database.utils

import com.poulastaa.kyoku.shardmanager.app.core.domain.repository.LocalShardUpdateDatasource
import com.poulastaa.kyoku.shardmanager.app.core.domain.utils.CURRENT_TIME
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking

fun createGenreArtistShardTables(db: LocalShardUpdateDatasource) = runBlocking {
    if (db.isGenreArtistShardDatabasePopulated()) return@runBlocking println("$CURRENT_TIME Artist Shard Tables are populated")

    coroutineScope {
        val artistDeferred = async { db.getAllArtist() }
        val genreArtistDeferred = async {
            db.getShardGenreArtistRelation().groupBy { it.genre }
        }

        val artistEntry = artistDeferred.await()
        val genreArtist = genreArtistDeferred.await()

        async { db.createShardArtistTable() }.await()
        artistEntry.chunked(5000).map { db.insertIntoShardArtist(it) }

        db.insertIntoShardGenreArtistRelation(genreArtist).also {
            println("$CURRENT_TIME Inserted Genres Most Popular Artists")
        }
    }
}
