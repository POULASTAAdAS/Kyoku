package com.poulastaa.kyoku.playlist.service

import com.google.gson.Gson
import com.poulastaa.kyoku.playlist.domain.model.DtoSong
import com.poulastaa.kyoku.playlist.domain.model.RedisKeys
import com.poulastaa.kyoku.playlist.utils.DebugUtils
import com.poulastaa.kyoku.playlist.utils.SongId
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import kotlin.time.toJavaDuration

@Service
class RedisCacheService(
    private val redis: RedisTemplate<String, Any>,
    private val gson: Gson,
) : RedisKeys() {
    /**
     * Cache is best effort: a Redis failure is logged and treated as a miss.
     */
    fun getSongsById(ids: List<SongId>): Map<SongId, DtoSong> = runCatching {
        val keys = ids.map { "${Group.SONG_BY_ID.prefix}$it" }
        val cached = redis.opsForValue().multiGet(keys) ?: return emptyMap()

        ids.zip(cached).mapNotNull { (id, song) ->
            // redis value is a Jackson-decoded map; Gson rebuilds the DTO field-by-field (keeps computed fields such as poster)
            song?.let { id to gson.fromJson(gson.toJson(it), DtoSong::class.java) }
        }.toMap()
    }.getOrElse {
        DebugUtils.d("redis read failed, treating as cache miss: ${it.message}")
        emptyMap()
    }

    fun setSongById(songs: List<DtoSong>) {
        if (songs.isEmpty()) return

        runCatching { Group.SONG_BY_ID.multiSet(songs.associateBy { it.id.toString() }) }
            .onFailure { DebugUtils.d("redis write failed: ${it.message}") }
    }

    private inline fun <reified V : Any> Group.multiSet(
        data: Map<String, V>,
    ) {
        redis.executePipelined {
            data.forEach {
                redis.opsForValue().set("${this.prefix}${it.key}", it.value, this.expTime.toJavaDuration())
            }
            null
        }
    }
}
