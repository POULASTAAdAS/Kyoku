package com.poulastaa.core.database.repository.suggestion

import com.google.gson.Gson
import com.poulastaa.core.database.mapper.toDtoPrevSong
import com.poulastaa.core.domain.model.DtoPrevSong
import com.poulastaa.core.domain.repository.LocalCoreCacheDatasource
import com.poulastaa.core.domain.repository.RedisKeys
import com.poulastaa.core.domain.repository.SongId
import com.poulastaa.core.domain.repository.suggestion.LocalSuggestionCacheDatasource
import redis.clients.jedis.JedisPool

class RedisLocalSuggestionDatasource(
    private val gson: Gson,
    private val redisPool: JedisPool,
    private val core: LocalCoreCacheDatasource,
) : LocalSuggestionCacheDatasource, RedisKeys() {
    override fun setPrevSongById(song: DtoPrevSong) {
        redisPool.resource.use { jedis ->
            jedis.setex(
                "${Group.PREV_SONG}:${song.id}",
                Group.PREV_SONG.expTime,
                gson.toJson(song)
            )
        }
    }

    override fun setPrevSongById(list: List<DtoPrevSong>) {
        if (list.isEmpty()) return

        redisPool.resource.use { jedis ->
            val pipeline = jedis.pipelined()

            list.associateBy { it.id }.forEach { (k, v) ->
                pipeline.setex(
                    "${Group.PREV_SONG}:$k",
                    Group.GENRE.expTime,
                    gson.toJson(v)
                )
            }

            pipeline.sync()
        }
    }

    override fun cachePrevSongById(list: List<SongId>): List<DtoPrevSong> =
        core.cacheSongById(list).map { it.toDtoPrevSong() }.ifEmpty {
            redisPool.resource.use { jedis ->
                jedis.mget(*list.map { "${Group.PREV_SONG}:$it" }.toTypedArray())
                    .mapNotNull { it }
                    .map { gson.fromJson(it, DtoPrevSong::class.java) }
            }
        }

    override fun cachePrevSongById(songId: SongId): DtoPrevSong? =
        core.cacheSongById(songId)?.toDtoPrevSong() ?: redisPool.resource.use { jedis ->
            jedis.get("${Group.PREV_SONG}:$songId")
        }?.let { gson.fromJson(it, DtoPrevSong::class.java) }
}