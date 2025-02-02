package com.poulastaa.kyoku.shardmanager.app.core.database.repository

import com.poulastaa.kyoku.shardmanager.app.core.database.dao.kyoku.DaoArtist
import com.poulastaa.kyoku.shardmanager.app.core.database.dao.shard.genre_artist.ShardDaoArtist
import com.poulastaa.kyoku.shardmanager.app.core.database.dao.shard.suggestion.ShardDaoSong
import com.poulastaa.kyoku.shardmanager.app.core.database.entity.shard.genre_artist.ShardEntityArtist
import com.poulastaa.kyoku.shardmanager.app.core.database.entity.shard.suggestion.ShardEntitySong
import com.poulastaa.kyoku.shardmanager.app.core.database.toDbArtistDto
import com.poulastaa.kyoku.shardmanager.app.core.domain.repository.LocalShardUpdateDatasource
import com.poulastaa.kyoku.shardmanager.app.plugins.kyokuDbQuery
import com.poulastaa.kyoku.shardmanager.app.plugins.popularDbQuery
import com.poulastaa.kyoku.shardmanager.app.plugins.shardGenreArtistDbQuery
import kotlinx.coroutines.*

class ExposedLocalShardUpdateDatasource private constructor() : LocalShardUpdateDatasource {
    private fun updateArtistPopularityOnceADay() { // todo will be removed
        CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                delay(12 * 60 * 60 * 1000) // wait for 12 hours

                // update artist popularity
                val artist =
                    kyokuDbQuery { DaoArtist.all().map { it.toDbArtistDto() } }

                artist.map { dto ->
                    async {
                        shardGenreArtistDbQuery {
                            ShardDaoArtist.findById(dto.id)?.let { table ->
                                if (dto.popularity != table.popularity) table.popularity = dto.popularity
                            }
                        }
                    }
                }.awaitAll()
            }
        }
    }

    override suspend fun updateSongPopularity() {
        coroutineScope {
            val song = kyokuDbQuery { DaoArtist.all().map { it.toDbArtistDto() } }

            popularDbQuery {
                song.chunked(5000).map { list ->
                    async {
                        list.map { dto ->
                            ShardDaoSong.find {
                                ShardEntitySong.id eq dto.id
                            }.firstOrNull()?.let { dao ->
                                if (dao.popularity != dto.popularity) dao.popularity = dto.popularity
                            }
                        }
                    }
                }.awaitAll()
            }
        }
    }

    override suspend fun updateArtistPopularity() {
        coroutineScope {
            val artist = kyokuDbQuery { DaoArtist.all().map { it.toDbArtistDto() } }

            shardGenreArtistDbQuery {
                artist.chunked(300).map { list ->
                    async {
                        list.map { dto ->
                            ShardDaoArtist.find {
                                ShardEntityArtist.id eq dto.id
                            }.firstOrNull()?.let { dao ->
                                if (dao.popularity != dao.popularity) dao.popularity = dto.popularity
                            }
                        }
                    }
                }.awaitAll()
            }
        }
    }

    override suspend fun updateGenreMostPopularArtists() {
        coroutineScope {

        }
    }

    override suspend fun updateArtistMostPopularSongs() {
        coroutineScope {

        }
    }

    override suspend fun updateCountryMostPopularSongs() {
        coroutineScope {

        }
    }

    override suspend fun updateYearMostPopularSongs() {
        coroutineScope {

        }
    }

    companion object {
        @Volatile
        private var INSTANCE: LocalShardUpdateDatasource? = null

        fun instance(): LocalShardUpdateDatasource = INSTANCE ?: synchronized(this) {
            INSTANCE ?: ExposedLocalShardUpdateDatasource().also { INSTANCE = it }
        }
    }
}