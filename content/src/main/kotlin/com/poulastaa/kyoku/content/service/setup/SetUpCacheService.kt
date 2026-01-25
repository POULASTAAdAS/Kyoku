package com.poulastaa.kyoku.content.service.setup

import com.poulastaa.kyoku.content.model.RedisKeys
import com.poulastaa.kyoku.content.model.dto.DtoGenre
import org.springframework.data.redis.core.DefaultTypedTuple
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.ScanOptions
import org.springframework.data.redis.core.ZSetOperations
import org.springframework.stereotype.Service
import kotlin.time.toJavaDuration

@Service
class SetUpCacheService(
    private val redis: RedisTemplate<String, Any>,
) : RedisKeys() {
    fun cacheGenrePerPage(
        page: Int,
        size: Int,
        query: String?,
    ): List<DtoGenre> {
        val start = page * size
        val end = start + size - 1

        return query?.let { q ->
            val cacheKey = "${Group.GENRE_BY_QUERY.prefix}:$q:RESULTS"

            // Cache hit
            val listSize = redis.opsForList().size(cacheKey) ?: 0
            if (listSize > 0 && start < listSize) {
                val cached = redis.opsForList().range(
                    cacheKey, start.toLong(),
                    end.coerceAtMost(listSize.toInt() - 1).toLong()
                )

                if (!cached.isNullOrEmpty()) return cached.mapNotNull { it as? DtoGenre }
            }

            // Cache miss - rebuild
            val keys = scanKeys("${Group.GENRE_BY_TYPE.prefix}${q.lowercase()}*")
            if (keys.isEmpty()) return emptyList()

            val list = keys.flatMap { key ->
                redis.opsForZSet().reverseRange(key, 0, -1)
                    ?.mapNotNull { it as? DtoGenre }
                    ?: emptyList()
            }.distinctBy { it.id }.sortedByDescending { it.popularity }

            if (list.isNotEmpty()) {
                redis.opsForList().rightPushAll(cacheKey, list.toTypedArray())
                redis.expire(cacheKey, Group.GENRE_BY_QUERY.expTime.value.toJavaDuration())
            }

            list.drop(start).take(size)
        } ?: run {
            redis.opsForZSet().reverseRange(Group.GENRE_BY_POPULARITY.prefix, start.toLong(), end.toLong())
                ?.mapNotNull { it as? DtoGenre } ?: emptyList()
        }
    }

    private fun scanKeys(pattern: String): List<String> {
        val keys = mutableListOf<String>()

        redis.execute({ connection ->
            val scanOptions = ScanOptions.scanOptions()
                .match(pattern)
                .count(100)
                .build()

            val cursor = connection.keyCommands().scan(scanOptions)

            while (cursor.hasNext()) {
                keys.add(String(cursor.next(), Charsets.UTF_8))
            }
        })

        return keys
    }

    fun setAllGenreByType(list: List<DtoGenre>) {
        if (list.isEmpty()) return

        // Group genres by type and store each type as a sorted set
        list.groupBy { it.type.lowercase() }.forEach { (type, genres) ->
            val key = "${Group.GENRE_BY_TYPE.prefix}$type"
            val tuples = genres.map {
                DefaultTypedTuple(
                    it,
                    it.popularity.toDouble()
                ) as ZSetOperations.TypedTuple<Any>
            }.toSet()

            redis.opsForZSet().add(key, tuples)
            redis.expire(key, Group.GENRE_BY_TYPE.expTime.value.toJavaDuration())
        }
    }

    fun setAllGenreByPopularity(list: List<DtoGenre>) {
        if (list.isEmpty()) return

        Group.GENRE_BY_POPULARITY.multiSetSorted(list.map {
            DefaultTypedTuple(
                it,
                it.popularity.toDouble()
            ) as ZSetOperations.TypedTuple<Any>
        }.toSet())
    }

    private fun Group.multiSetSorted(
        data: Set<ZSetOperations.TypedTuple<Any>>,
    ) {
        redis.executePipelined {
            // 1. Add the data to the Sorted Set
            redis.opsForZSet().add(this.prefix, data)
            // 2. Set the expiration on the key itself
            redis.expire(this.prefix, this.expTime.value.toJavaDuration())

            null
        }
    }
}