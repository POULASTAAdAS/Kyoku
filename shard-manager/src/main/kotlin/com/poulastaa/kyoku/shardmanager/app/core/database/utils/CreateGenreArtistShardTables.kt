package com.poulastaa.kyoku.shardmanager.app.core.database.utils

import com.poulastaa.kyoku.shardmanager.app.core.domain.repository.LocalShardUpdateDatasource
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking

fun createGenreArtistShardTables(db: LocalShardUpdateDatasource) = runBlocking {
    if (db.isGenreArtistShardDatabasePopulated()) return@runBlocking println("Artist Shard Tables are populated")

    coroutineScope {
        val artistDeferred = async { db.getAllArtist() }
        val genreArtistDeferred = async {
            db.getShardGenreArtistRelation().groupBy { it.genre }
        }

        val artistEntry = artistDeferred.await()
        val genreArtist = genreArtistDeferred.await()

        db.insertIntoShardArtist(artistEntry)
        db.createShardArtistTable()
        artistEntry.chunked(5000).map { db.insertIntoShardArtist(it) }

        db.insertIntoShardGenreArtistRelation(genreArtist).also {
            println("Inserted Genres Most Popular Artists")
        }
    }
}
