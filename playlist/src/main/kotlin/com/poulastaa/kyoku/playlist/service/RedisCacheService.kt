package com.poulastaa.kyoku.playlist.service

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.poulastaa.kyoku.playlist.domain.model.DtoSong
import com.poulastaa.kyoku.playlist.domain.model.RedisKeys
import com.poulastaa.kyoku.playlist.utils.SongTitle
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import kotlin.time.toJavaDuration

@Service
class RedisCacheService(
    private val redis: RedisTemplate<String, Any>,
    private val gson: Gson,
) : RedisKeys() {
    fun cacheSongByTitle(titles: List<SongTitle>): Map<SongTitle, DtoSong> {
        val keys = titles.map { "${Group.SONG_BY_TITLE.prefix}$it" }
        val cached = redis.opsForValue().multiGet(keys) ?: emptyList()
        if (cached.isEmpty()) return emptyMap()

        return titles.zip(cached).mapNotNull { (title, song) ->
            song?.let { title to gson.fromJson<DtoSong>(gson.toJson(song), object : TypeToken<DtoSong>() {}.type) }
        }.toMap()
    }

    fun setSongByTitle(songs: List<DtoSong>) {
        if (songs.isEmpty()) return
        Group.SONG_BY_TITLE.multiSet(songs.associateBy { it.title })
    }

    fun setSongById(songs: List<DtoSong>) {
        if (songs.isEmpty()) return
        Group.SONG_BY_ID.multiSet(songs.associateBy { it.id.toString() })
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