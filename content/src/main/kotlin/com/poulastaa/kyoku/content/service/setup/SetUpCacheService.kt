package com.poulastaa.kyoku.content.service.setup

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.poulastaa.kyoku.content.model.RedisKeys
import com.poulastaa.kyoku.content.model.dto.DtoGenre
import com.poulastaa.kyoku.content.utils.HasMoreGenre
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import kotlin.time.toJavaDuration

@Service
class SetUpCacheService(
    private val redis: RedisTemplate<String, Any>,
    private val gson: Gson,
) : RedisKeys() {
    fun cacheGenrePerPage(
        offSet: Int,
        size: Int,
        query: String?,
    ): Pair<List<DtoGenre>, HasMoreGenre> {
        return gson.fromJson<List<DtoGenre>>(
            gson.toJson(redis.opsForValue().get(Group.GENRE.prefix) ?: emptyList<Any>()),
            object : TypeToken<List<DtoGenre>>() {}.type
        ).takeIf { it.isNotEmpty() }?.let { list ->
            val newList = if (query.isNullOrEmpty()) list.drop(offSet)
            else list.filter { it.type.startsWith(query, ignoreCase = true) }
                .drop(offSet)

            newList.take(size) to (newList.size > size)
        } ?: Pair(emptyList(), false)
    }

    fun setAllGenre(list: List<DtoGenre>) {
        if (list.isEmpty()) return

        redis.opsForValue().set(
            Group.GENRE.prefix,
            list,
            Group.GENRE.expTime.value.toJavaDuration()
        )
    }
}