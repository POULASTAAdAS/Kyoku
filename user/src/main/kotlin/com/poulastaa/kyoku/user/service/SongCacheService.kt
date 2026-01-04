package com.poulastaa.kyoku.user.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.poulastaa.kyoku.user.domain.model.DtoSong
import com.poulastaa.kyoku.user.utils.SongId
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import java.time.Duration

/**
 * Redis caching service for songs with multiple access patterns
 * 
 * Cache Strategy:
 * 1. song:id:{id} - Cache by song ID (TTL: 24 hours)
 * 2. song:title:{title} - Cache by exact title (TTL: 12 hours)
 * 3. song:batch:{hash} - Cache batch queries (TTL: 1 hour)
 */
@Service
class SongCacheService(
    private val redisTemplate: RedisTemplate<String, Any>,
    private val objectMapper: ObjectMapper,
) {
    companion object {
        private const val SONG_BY_ID_PREFIX = "song:id:"
        private const val SONG_BY_TITLE_PREFIX = "song:title:"
        private const val SONG_BATCH_PREFIX = "song:batch:"
        private const val SONG_SEARCH_PREFIX = "song:search:"

        private val ID_CACHE_TTL = Duration.ofHours(24)
        private val TITLE_CACHE_TTL = Duration.ofHours(12)
        private val BATCH_CACHE_TTL = Duration.ofHours(1)
        private val SEARCH_CACHE_TTL = Duration.ofMinutes(30)
    }

    // ============= Cache by ID =============
    fun getSongById(id: SongId): DtoSong? {
        val key = "$SONG_BY_ID_PREFIX$id"
        return redisTemplate.opsForValue().get(key) as? DtoSong
    }

    fun setSongById(song: DtoSong) {
        val key = "$SONG_BY_ID_PREFIX${song.id}"
        redisTemplate.opsForValue().set(key, song, ID_CACHE_TTL)
    }

    fun setSongsById(songs: List<DtoSong>) {
        songs.forEach { setSongById(it) }
    }

    // ============= Cache by Title =============
    fun getSongByTitle(title: String): DtoSong? {
        val normalizedTitle = title.lowercase().trim()
        val key = "$SONG_BY_TITLE_PREFIX$normalizedTitle"
        return redisTemplate.opsForValue().get(key) as? DtoSong
    }

    fun setSongByTitle(song: DtoSong) {
        val normalizedTitle = song.title.lowercase().trim()
        val key = "$SONG_BY_TITLE_PREFIX$normalizedTitle"
        redisTemplate.opsForValue().set(key, song, TITLE_CACHE_TTL)
    }

    fun setSongsByTitle(songs: List<DtoSong>) {
        songs.forEach { setSongByTitle(it) }
    }

    // ============= Batch Cache =============
    /**
     * Cache batch query results using hash of titles as key
     * Useful for Spotify playlist import where same combinations are queried
     */
    fun getBatchSongs(titles: List<String>): List<DtoSong>? {
        val normalizedTitles = titles.map { it.lowercase().trim() }.sorted()
        val batchKey = "$SONG_BATCH_PREFIX${normalizedTitles.hashCode()}"

        @Suppress("UNCHECKED_CAST")
        return redisTemplate.opsForValue().get(batchKey) as? List<DtoSong>
    }

    fun setBatchSongs(titles: List<String>, songs: List<DtoSong>) {
        val normalizedTitles = titles.map { it.lowercase().trim() }.sorted()
        val batchKey = "$SONG_BATCH_PREFIX${normalizedTitles.hashCode()}"
        redisTemplate.opsForValue().set(batchKey, songs, BATCH_CACHE_TTL)
    }

    // ============= Search Cache =============
    /**
     * Cache search results (short TTL due to frequent updates)
     */
    fun getSearchResults(searchTerm: String, page: Int, size: Int): List<DtoSong>? {
        val normalizedTerm = searchTerm.lowercase().trim()
        val key = "$SONG_SEARCH_PREFIX${normalizedTerm}:$page:$size"

        @Suppress("UNCHECKED_CAST")
        return redisTemplate.opsForValue().get(key) as? List<DtoSong>
    }

    fun setSearchResults(searchTerm: String, page: Int, size: Int, songs: List<DtoSong>) {
        val normalizedTerm = searchTerm.lowercase().trim()
        val key = "$SONG_SEARCH_PREFIX${normalizedTerm}:$page:$size"
        redisTemplate.opsForValue().set(key, songs, SEARCH_CACHE_TTL)
    }

    // ============= Multi-Get Optimization =============
    /**
     * Get multiple songs by IDs in a single Redis call (pipeline)
     * Much faster than individual gets
     */
    fun getSongsByIds(ids: List<SongId>): Map<SongId, DtoSong> {
        if (ids.isEmpty()) return emptyMap()

        val keys = ids.map { "$SONG_BY_ID_PREFIX$it" }
        val results = redisTemplate.opsForValue().multiGet(keys) ?: return emptyMap()

        return ids.zip(results)
            .mapNotNull { (id, value) ->
                (value as? DtoSong)?.let { id to it }
            }
            .toMap()
    }

    /**
     * Get multiple songs by titles in a single Redis call (pipeline)
     */
    fun getSongsByTitles(titles: List<String>): Map<String, DtoSong> {
        if (titles.isEmpty()) return emptyMap()

        val normalizedTitles = titles.map { it.lowercase().trim() }
        val keys = normalizedTitles.map { "$SONG_BY_TITLE_PREFIX$it" }
        val results = redisTemplate.opsForValue().multiGet(keys) ?: return emptyMap()

        return titles.zip(results)
            .mapNotNull { (title, value) ->
                (value as? DtoSong)?.let { title to it }
            }
            .toMap()
    }

    // ============= Cache Invalidation =============
    fun invalidateSong(id: SongId) {
        redisTemplate.delete("$SONG_BY_ID_PREFIX$id")
    }

    fun invalidateByTitle(title: String) {
        val normalizedTitle = title.lowercase().trim()
        redisTemplate.delete("$SONG_BY_TITLE_PREFIX$normalizedTitle")
    }

    /**
     * Clear all search caches when songs are updated
     * Use carefully - expensive operation
     */
    fun invalidateAllSearchCaches() {
        val pattern = "$SONG_SEARCH_PREFIX*"
        val keys = redisTemplate.keys(pattern)
        if (keys.isNotEmpty()) {
            redisTemplate.delete(keys)
        }
    }
}
