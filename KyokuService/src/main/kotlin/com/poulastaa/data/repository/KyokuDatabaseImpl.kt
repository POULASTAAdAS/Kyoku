package com.poulastaa.data.repository

import com.poulastaa.data.dao.ArtistDao
import com.poulastaa.data.mappers.toArtistResult
import com.poulastaa.domain.model.ResultArtist
import com.poulastaa.domain.repository.DatabaseRepository
import com.poulastaa.domain.table.ArtistTable
import com.poulastaa.domain.table.SongTable
import com.poulastaa.domain.table.relation.SongArtistRelationTable
import com.poulastaa.plugins.query
import kotlinx.coroutines.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.update

class KyokuDatabaseImpl : DatabaseRepository {
    override fun updateSongPointByOne(list: List<Long>) {
        CoroutineScope(Dispatchers.IO).launch {
            query {
                SongTable.update({ SongTable.id inList list }) {
                    with(SqlExpressionBuilder) {
                        it[points] = points + 1
                    }
                }
            }
        }
    }

    override fun updateArtistPointByOne(list: List<Long>) {
        CoroutineScope(Dispatchers.IO).launch {
            query {
                ArtistTable.update({ ArtistTable.id inList list }) {
                    with(SqlExpressionBuilder) {
                        it[points] = points + 1
                    }
                }
            }
        }
    }

    override suspend fun getArtistOnSongId(songId: Long): List<ResultArtist> = query {
        val artistIdList = SongArtistRelationTable.select {
            SongArtistRelationTable.songId eq songId
        }.map {
            it[SongArtistRelationTable.artistId]
        }

        ArtistDao.find {
            ArtistTable.id inList artistIdList
        }.map {
            it.toArtistResult()
        }
    }

    override suspend fun getArtistOnSongIdList(
        list: List<Long>,
    ): List<Pair<Long, List<ResultArtist>>> = coroutineScope {
        val idMap = list.map {
            async {
                it to query {
                    SongArtistRelationTable.select {
                        SongArtistRelationTable.songId eq it
                    }.map {
                        it[SongArtistRelationTable.artistId]
                    }
                }
            }
        }.awaitAll()

        idMap.map { pair ->
            async {
                pair.first to pair.second.let { artistIdList ->
                    query {
                        ArtistDao.find {
                            ArtistTable.id inList artistIdList
                        }.map {
                            it.toArtistResult()
                        }
                    }
                }
            }
        }.awaitAll()
    }
}