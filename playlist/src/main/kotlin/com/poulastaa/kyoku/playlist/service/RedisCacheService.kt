package com.poulastaa.kyoku.playlist.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.poulastaa.kyoku.playlist.domain.model.DtoSong
import com.poulastaa.kyoku.playlist.domain.model.RedisKeys
import com.poulastaa.kyoku.playlist.utils.SongTitle
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service

@Service
class RedisCacheService(
    private val redis: RedisTemplate<String, Any>,
    private val mapper: ObjectMapper,
) : RedisKeys() {
    fun cacheSongByTitle(titles: List<SongTitle>): Map<SongTitle, DtoSong> {
        val cached = redis.opsForValue().multiGet(titles) ?: emptyList()
        if (cached.isEmpty()) return emptyMap()

        return titles.zip(cached).mapNotNull { (title, song) ->
            song?.let { title to mapper.convertValue(it, DtoSong::class.java) }
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
                redis.opsForValue().set("${this.prefix}${it.key}", it.value, this.expTime)
            }
            null
        }
    }
}