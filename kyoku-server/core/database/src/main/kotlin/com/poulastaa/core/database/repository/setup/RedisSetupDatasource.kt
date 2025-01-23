package com.poulastaa.core.database.repository.setup

import com.google.gson.Gson
import com.poulastaa.core.domain.model.DtoGenre
import com.poulastaa.core.domain.model.DtoSong
import com.poulastaa.core.domain.repository.GenreId
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

    override fun cacheGenre(id: List<GenreId>): List<DtoGenre> {
        if (id.isEmpty()) return emptyList()
        redisPool.resource.use { jedis ->
            val genreJson = jedis.mget(*id.map { "${Group.GENRE}:$it" }.toTypedArray())

            return genreJson.filterNotNull().map {
                gson.fromJson(it, DtoGenre::class.java)
            }
        }
    }

    override fun cacheGenreByQuery(query: String, size: Int): List<DtoGenre> {
        redisPool.resource.use { jedis ->
            val genre = jedis.mget("${Group.GENRE_TITLE}:$query*")
            val idList = genre.filterNotNull().map { it.toInt() }.take(size)

            return cacheGenre(idList)
        }
    }

    override fun setGenreById(list: List<DtoGenre>) = core.setGenreById(list)

    override fun setGenreIdByName(list: Map<String, GenreId>) {
        redisPool.resource.use { jedis ->
            val pipeline = jedis.pipelined()

            list.forEach { (name, id) ->
                pipeline.setex(
                    "${Group.GENRE_TITLE}:$name",
                    Group.GENRE_TITLE.expTime,
                    id.toString()
                )
            }

            pipeline.sync()
        }
    }
}