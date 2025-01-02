package com.poulastaa.core.database.repository.setup

import com.google.gson.Gson
import com.poulastaa.core.domain.model.DtoSong
import com.poulastaa.core.domain.repository.LocalCoreCacheDatasource
import com.poulastaa.core.domain.repository.RedisKeys
import com.poulastaa.core.domain.repository.setup.LocalSetupCacheDatasource
import redis.clients.jedis.JedisPool
import redis.clients.jedis.params.ScanParams

class RedisSetupDatasource(
    private val gson: Gson,
    private val redisPool: JedisPool,
    private val core: LocalCoreCacheDatasource,
) : LocalSetupCacheDatasource, RedisKeys() {
    override fun cacheSongByTitle(list: List<String>): List<DtoSong> { // todo set just id to reduce overhead
        val result = mutableListOf<DtoSong>()
        val filterExclusions = listOf("lofi", "remix", "slowed", "mashup", "ringtone")

        redisPool.resource.use { jedis ->
            var cursor = ScanParams.SCAN_POINTER_START

            do {
                val scanResult = jedis.scan(
                    cursor, ScanParams()
                        .match("${Group.SONG_TITLE}:*")
                        .count(500)
                )

                cursor = scanResult.cursor

                val filteredKeys = scanResult.result.filter { key ->
                    list.any { title -> key.contains(title, ignoreCase = true) } &&
                            filterExclusions.none { key.contains(it, ignoreCase = true) }
                }

                if (filteredKeys.isNotEmpty()) jedis.pipelined().use { pipeline ->
                    val response = filteredKeys.map { key -> pipeline.get(key) }
                    pipeline.sync()

                    response.mapNotNull { it.get() }
                        .map { gson.fromJson(it, DtoSong::class.java) }
                        .forEach { result.add(it) }
                }
            } while (cursor != ScanParams.SCAN_POINTER_START)
        }

        return result.toList()
    }

    override fun setSongIdByTitle(list: List<DtoSong>) {
        if (list.isEmpty()) return

        redisPool.resource.use { jedis ->
            val pipeline = jedis.pipelined()

            list.forEach { song ->
                pipeline.setex(
                    "${Group.SONG_TITLE}:${song.title}",
                    Group.SONG_TITLE.expTime,
                    gson.toJson(song)
                )
            }

            pipeline.sync()
        }
    }

    override fun setSongById(list: List<DtoSong>) = core.setSongById(list)
}