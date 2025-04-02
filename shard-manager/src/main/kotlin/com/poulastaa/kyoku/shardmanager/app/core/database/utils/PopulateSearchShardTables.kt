package com.poulastaa.kyoku.shardmanager.app.core.database.utils

import com.poulastaa.kyoku.shardmanager.app.core.domain.repository.LocalShardUpdateDatasource
import com.poulastaa.kyoku.shardmanager.app.core.domain.utils.CURRENT_TIME
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking

fun populatePagingShardTables(db: LocalShardUpdateDatasource) = runBlocking {
    if (db.isPagingShardDatabasePopulated()) return@runBlocking println("$CURRENT_TIME Paging Shard Tables are populated")

    println("$CURRENT_TIME inserting into paging shard")

    coroutineScope {
        val artistDeferred = async { db.getAllArtist() }
        val songsDeferred = async { db.getAllSongs() }
        val albumDeferred = async { db.getAllAlbums() }

        val artistAlbumDef = async { db.getArtistAlbumRelation() }
        val artistSong = async { db.getArtistSongRelation() }

        val artist = async {
            val artist = artistDeferred.await()

            artist.chunked(5000).map {
                println("inserting artist")
                db.insertShardPagingEntityArtist(it)
            }
        }

        val album = async {
            val album = albumDeferred.await()
            album.chunked(5000).map {
                println("inserting album")
                db.insertShardPagingEntityAlbum(it)
            }
        }

        val songs = async {
            val songs = songsDeferred.await()
            songs.chunked(5000).map {
                println("inserting song")
                db.insertShardPagingEntitySong(it)
            }
        }

        listOf(
            artist,
            album,
            songs
        ).awaitAll()

        println("$CURRENT_TIME finished inserting into paging shard")

        listOf(
            async { db.insertArtistAlbumRelation(artistAlbumDef.await()) },
            async { db.insertArtistSongRelation(artistSong.await()) },
        ).awaitAll()
    }
    println("$CURRENT_TIME finished inserting relation into paging shard")
}
