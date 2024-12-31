package com.poulastaa.core.database.repository

import com.google.gson.Gson
import com.poulastaa.core.domain.model.DBUserDto
import com.poulastaa.core.domain.model.SongDto
import com.poulastaa.core.domain.model.UserType
import com.poulastaa.core.domain.repository.LocalCoreCacheDatasource
import com.poulastaa.core.domain.repository.auth.Email
import redis.clients.jedis.JedisPool
import redis.clients.jedis.params.ScanParams
import redis.clients.jedis.params.SetParams

class RedisLocalCoreDatasource(
    private val redisPool: JedisPool,
    private val gson: Gson,
) : LocalCoreCacheDatasource {
    private object Group {
        const val USER = "USER"
        const val SONG = "SONG"
    }

    override fun cacheUsersByEmail(
        email: String,
        type: UserType,
    ): DBUserDto? = redisPool.resource.use { jedis ->
        jedis.get("${Group.USER}:${type.name}:$email")
    }?.let { string ->
        gson.fromJson(string, DBUserDto::class.java)
    }

    override fun setUserByEmail(
        key: Email,
        type: UserType,
        value: DBUserDto,
    ) {
        redisPool.resource.use { jedis ->
            jedis.set(
                "${Group.USER}:${type.name}:$key",
                gson.toJson(value),
                SetParams.setParams().ex(15 * 60) // 15 minute
            )
        }
    }

    override fun getSongByTitle(list: List<String>): List<SongDto> {
        val result = mutableListOf<SongDto>()
        var cursor = "0"
        redisPool.resource.use { jedis ->
            do {
                list.forEach { title ->
                    val scanResult = jedis.scan(cursor, ScanParams().match("${Group.SONG}:$title").count(5))
                    cursor = scanResult.cursor

                    val song = scanResult.result.map { key ->
                        val string = jedis.get(key)
                        gson.fromJson(string, SongDto::class.java)
                    }.filterNot { song ->
                        listOf("remix", "mashup", "lofi", "slowed").all { keyword ->
                            song.title.contains(keyword, ignoreCase = true)
                        }
                    }
                }
            } while (cursor != "0")
        }
    }
}