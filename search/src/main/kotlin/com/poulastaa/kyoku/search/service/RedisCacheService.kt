package com.poulastaa.kyoku.search.service

import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.poulastaa.kyoku.search.domain.model.RedisKeys
import com.poulastaa.kyoku.search.domain.model.dto.DtoArtist
import com.poulastaa.kyoku.search.utils.Country
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import kotlin.time.toJavaDuration

@Service
class RedisCacheService(
    private val redis: RedisTemplate<String, Any>,
    private val gson: Gson,
) : RedisKeys() {
    fun setMostPopularArtistsByCountry(
        data: List<DtoArtist>,
        key: Country,
    ) = Group.POPULAR_ARTIST_BY_COUNTRY.setList(data, key)

    fun getMostPopularArtistByCountry(
        size: Int,
        page: Int,
        key: Country,
    ) = Group.POPULAR_ARTIST_BY_COUNTRY.getList<DtoArtist>(key)
        ?.drop(page * size)
        ?.take(size)

    private inline fun <reified DATA : Any> Group.setList(data: List<DATA>, key: String) {
        redis.opsForValue().set(
            this.buildKey(key),
            data,
            this.expTime.time.toJavaDuration()
        )
    }

    private inline fun <reified DATA : Any> Group.getList(key: String): List<DATA>? {
        val raw = redis.opsForValue().get(this.buildKey(key)) ?: return null
        return gson.fromJson(
            gson.toJson(raw),
            object : TypeToken<List<DATA>>() {}.type
        )
    }
}