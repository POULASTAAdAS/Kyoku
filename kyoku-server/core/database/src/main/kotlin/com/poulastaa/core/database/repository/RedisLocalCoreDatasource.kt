package com.poulastaa.core.database.repository

import com.google.gson.Gson
import com.poulastaa.core.domain.model.DtoDBUser
import com.poulastaa.core.domain.model.DtoPlaylist
import com.poulastaa.core.domain.model.UserType
import com.poulastaa.core.domain.repository.LocalCoreCacheDatasource
import com.poulastaa.core.domain.repository.RedisKeys
import com.poulastaa.core.domain.repository.auth.Email
import redis.clients.jedis.JedisPool
import redis.clients.jedis.params.SetParams

class RedisLocalCoreDatasource(
    private val redisPool: JedisPool,
    private val gson: Gson,
) : LocalCoreCacheDatasource, RedisKeys() {
    override fun cacheUsersByEmail(
        email: String,
        type: UserType,
    ): DtoDBUser? = redisPool.resource.use { jedis ->
        jedis.get("${Group.USER}:${type.name}:$email")
    }?.let { string ->
        gson.fromJson(string, DtoDBUser::class.java)
    }

    override fun setUserByEmail(
        key: Email,
        type: UserType,
        value: DtoDBUser,
    ) {
        redisPool.resource.use { jedis ->
            jedis.set(
                "${Group.USER}:${type.name}:$key",
                gson.toJson(value),
                SetParams.setParams().ex(15 * 60) // 15 minute
            )
        }
    }

    override fun setPlaylist(playlistDto: DtoPlaylist) {
        redisPool.resource.use { jedis ->
            jedis.set(
                "${Group.PLAYLIST}:${playlistDto.id}",
                gson.toJson(playlistDto),
                SetParams.setParams().ex(15 * 60) // 10 minute
            )
        }
    }
}