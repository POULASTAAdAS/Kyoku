package com.poulastaa.core.database.repository.setup

import com.google.gson.Gson
import com.poulastaa.core.database.mapper.toDtoPrevArtist
import com.poulastaa.core.domain.model.*
import com.poulastaa.core.domain.repository.ArtistId
import com.poulastaa.core.domain.repository.GenreId
import com.poulastaa.core.domain.repository.LocalCoreCacheDatasource
import com.poulastaa.core.domain.repository.RedisKeys
import com.poulastaa.core.domain.repository.auth.Email
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

    override fun setUserByEmail(key: Email, type: UserType, value: DtoDBUser) = core.setUserByEmail(key, type, value)

    override fun cacheGenreById(list: List<GenreId>): List<DtoGenre> {
        if (list.isEmpty()) return emptyList()
        return core.cacheGenreById(list)
    }

    override fun cacheGenreByName(query: String, size: Int): List<DtoGenre> {
        redisPool.resource.use { jedis ->
            val genre = jedis.mget("${Group.GENRE_TITLE}:$query*")
            val idList = genre.filterNotNull().map { it.toInt() }.take(size)

            return cacheGenreById(idList)
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

    override fun cachePrevGenreByUserId(userId: Long): List<DtoPrevGenre> {
        redisPool.resource.use { jedis ->
            val jsonList = jedis.lrange("${Group.RELATION_PREV_GENRE_USER}:$userId", 0, -1)
            return jsonList.map { gson.fromJson(it, DtoPrevGenre::class.java) }
        }
    }

    override fun setPrevGenreByUserId(
        userId: Long,
        data: List<DtoPrevGenre>,
    ) { // todo move to core cache if needed
        redisPool.resource.use { jedis ->
            val pipeline = jedis.pipelined()
            val key = "${Group.RELATION_PREV_GENRE_USER}:$userId"

            pipeline.del(key)
            data.forEach { genre ->
                pipeline.rpush(key, gson.toJson(genre))
            }

            pipeline.expire(key, Group.RELATION_PREV_GENRE_USER.expTime)

            pipeline.sync()
        }
    }

    override fun cachePrevArtistById(list: List<ArtistId>): List<DtoPrevArtist> {
        if (list.isEmpty()) return emptyList()
        val fromDtoArtist = core.cacheArtistById(list).map { it.toDtoPrevArtist() }

        if (fromDtoArtist.size == list.size) return fromDtoArtist

        val notFoundIdList = list.filterNot { artistId -> fromDtoArtist.any { it.id == artistId } }

        val cache = redisPool.resource.use { jedis ->
            jedis.mget(*notFoundIdList.map { "${Group.PREV_ARTIST}:${it}" }.toTypedArray())
                .mapNotNull { it }
                .map { gson.fromJson(it, DtoPrevArtist::class.java) }
        }

        return fromDtoArtist + cache
    }

    override fun cachePrevArtistByName(
        query: String,
        size: Int,
    ): List<DtoPrevArtist> {
        redisPool.resource.use { jedis ->
            val genre = jedis.mget("${Group.PREV_ARTIST_TITLE}:$query*")
            val idList = genre.filterNotNull().map { it.toLong() }.take(size)

            return cachePrevArtistById(idList)
        }
    }

    override fun setPrevArtistById(list: List<DtoPrevArtist>) {
        if (list.isEmpty()) return

        redisPool.resource.use { jedis ->
            val pipe = jedis.pipelined()

            list.forEach { artist ->
                pipe.setex(
                    "${Group.PREV_ARTIST}:${artist.id}",
                    Group.PREV_ARTIST.expTime,
                    gson.toJson(artist)
                )
            }

            pipe.sync()
        }
    }

    override fun setPrevArtistIdByName(list: Map<String, ArtistId>) {
        if (list.isEmpty()) return

        redisPool.resource.use { jedis ->
            val pipeline = jedis.pipelined()

            list.forEach { (title, artistId) ->
                pipeline.setex(
                    "${Group.PREV_ARTIST_TITLE}:$title",
                    Group.GENRE.expTime,
                    artistId.toString()
                )
            }

            pipeline.sync()
        }
    }

    override fun cacheArtistById(list: List<ArtistId>): List<DtoArtist> = core.cacheArtistById(list)
}