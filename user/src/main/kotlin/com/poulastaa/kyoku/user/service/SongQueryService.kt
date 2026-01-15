package com.poulastaa.kyoku.user.service
//
//import com.poulastaa.kyoku.user.database.entity.EntitySong
//import com.poulastaa.kyoku.user.database.repository.SongRepository
//import com.poulastaa.kyoku.user.domain.model.DtoSong
//import com.poulastaa.kyoku.user.utils.SongId
//import kotlinx.coroutines.*
//import org.springframework.data.domain.PageRequest
//import org.springframework.stereotype.Service
//import org.springframework.transaction.annotation.Transactional
//import kotlin.Boolean
//import kotlin.Int
//import kotlin.String
//import kotlin.also
//import kotlin.collections.List
//import kotlin.collections.any
//import kotlin.collections.associateBy
//import kotlin.collections.emptyList
//import kotlin.collections.filter
//import kotlin.collections.filterNotNull
//import kotlin.collections.firstOrNull
//import kotlin.collections.get
//import kotlin.collections.isNotEmpty
//import kotlin.collections.listOf
//import kotlin.collections.map
//import kotlin.collections.mapNotNull
//import kotlin.collections.none
//import kotlin.collections.plus
//import kotlin.collections.toSet
//import kotlin.let
//import kotlin.plus
//import kotlin.sequences.any
//import kotlin.sequences.associateBy
//import kotlin.sequences.filterNotNull
//import kotlin.sequences.plus
//import kotlin.sequences.toSet
//import kotlin.text.any
//import kotlin.text.associateBy
//import kotlin.text.contains
//import kotlin.text.lowercase
//import kotlin.text.plus
//import kotlin.text.replace
//import kotlin.text.toSet
//import kotlin.text.trim
//
///**
// * Optimized service for querying songs with Redis caching
// *
// * Performance Strategy:
// * 1. Check Redis first (O(1) lookup)
// * 2. If miss, query DB with optimized queries
// * 3. Cache results for future requests
// * 4. Use batch operations to minimize round trips
// */
//@Service
//class SongQueryService(
//    private val songRepository: SongRepository,
//    private val cacheService: SongCacheService,
//) {
//
//    /**
//     * Get songs by multiple titles - OPTIMIZED for Spotify playlist import
//     *
//     * Previous approach issues:
//     * - N queries (one per title)
//     * - Complex filtering in app layer
//     * - Sequential processing
//     *
//     * New approach:
//     * 1. Check batch cache first
//     * 2. Check individual title cache (multi-get)
//     * 3. Query DB for remaining in single query WITH QUALITY FILTERS
//     * 4. Cache all results
//     */
//    @Transactional(readOnly = true)
//    suspend fun getSongsByTitles(titles: List<String>): List<DtoSong> = coroutineScope {
//        if (titles.isEmpty()) return@coroutineScope emptyList()
//
//        // Step 1: Try batch cache (for repeated queries like popular playlists)
//        cacheService.getBatchSongs(titles)?.let { return@coroutineScope it }
//
//        // Step 2: Check individual caches (multi-get in one Redis call)
//        val normalizedTitles = titles.map { it.lowercase().trim() }
//        val cachedSongs = cacheService.getSongsByTitles(normalizedTitles)
//
//        // Step 3: Find which titles are not in cache
//        val uncachedTitles = normalizedTitles.filter { it !in cachedSongs.keys }
//
//        // Step 4: Query DB for uncached titles in SINGLE query
//        val dbSongs = if (uncachedTitles.isNotEmpty()) {
//            async(Dispatchers.IO) {
//                songRepository.findAllByTitlesIgnoreCase(uncachedTitles)
//                    .filter { song -> isQualitySong(song.title) }
//                    .map { it.toDto() }
//                    .also { songs ->
//                        // Cache individual results
//                        cacheService.setSongsById(songs)
//                        cacheService.setSongsByTitle(songs)
//                    }
//            }.await()
//        } else emptyList()
//
//        // Step 5: Combine results
//        val allSongs = cachedSongs.values + dbSongs
//
//        // Step 6: Cache batch result for future queries
//        cacheService.setBatchSongs(titles, allSongs)
//
//        allSongs
//    }
//
//    /**
//     * Get EXACT song matches with quality filters (no remixes, mashups, etc.)
//     * Best for: When you want the original version only
//     *
//     * Example: "Bohemian Rhapsody" -> Returns only the original Queen version,
//     *          not "Bohemian Rhapsody Remix", "Bohemian Rhapsody Slowed", etc.
//     */
//    @Transactional(readOnly = true)
//    suspend fun getExactSongsByTitles(titles: List<String>): List<DtoSong> = coroutineScope {
//        if (titles.isEmpty()) return@coroutineScope emptyList()
//
//        // Step 1: Try batch cache first
//        cacheService.getBatchSongs(titles)?.let { return@coroutineScope it }
//
//        // Step 2: Check individual caches (multi-get)
//        val normalizedTitles = titles.map { it.lowercase().trim() }
//        val cachedSongs = cacheService.getSongsByTitles(normalizedTitles)
//
//        // Step 3: Find uncached titles
//        val uncachedTitles = normalizedTitles.filter { it !in cachedSongs.keys }
//
//        // Step 4: Query DB with EXACT match + quality filters (single query!)
//        val dbSongs = if (uncachedTitles.isNotEmpty()) {
//            async(Dispatchers.IO) {
//                songRepository.findExactTitlesWithFilters(uncachedTitles)
//                    .map { it.toDto() }
//                    .also { songs ->
//                        cacheService.setSongsById(songs)
//                        cacheService.setSongsByTitle(songs)
//                    }
//            }.await()
//        } else emptyList()
//
//        val allSongs = cachedSongs.values + dbSongs
//        cacheService.setBatchSongs(titles, allSongs)
//
//        allSongs
//    }
//
//    /**
//     * Smart search: Try EXACT match first, fallback to SIMILAR matches
//     * Best for: Spotify import where title might not match exactly
//     *
//     * Strategy:
//     * 1. Try exact title match with filters (fast, high quality)
//     * 2. If not found, try similar/fuzzy match (slower, but finds alternatives)
//     */
//    @Transactional(readOnly = true)
//    suspend fun getSongsByTitlesWithFallback(titles: List<String>): List<DtoSong> = coroutineScope {
//        if (titles.isEmpty()) return@coroutineScope emptyList()
//
//        val normalizedTitles = titles.map { it.lowercase().trim() }
//
//        // Step 1: Try exact matches first
//        val exactMatches = async(Dispatchers.IO) {
//            songRepository.findExactTitlesWithFilters(normalizedTitles)
//                .map { it.toDto() }
//        }
//
//        val exactResults = exactMatches.await()
//        val foundTitles = exactResults.map { it.title.lowercase() }.toSet()
//
//        // Step 2: For titles not found exactly, try similar/fuzzy search
//        val notFoundTitles = normalizedTitles.filter { title ->
//            !foundTitles.any { found -> found == title }
//        }
//
//        val similarMatches = if (notFoundTitles.isNotEmpty()) {
//            notFoundTitles.map { title ->
//                async(Dispatchers.IO) {
//                    // Build regex pattern: "bohemian" -> ".*bohemian.*"
//                    val pattern = ".*${title.replace(" ", ".*")}.*"
//                    songRepository.findBySimilarTitleWithFilters(pattern, limit = 1)
//                        .firstOrNull()?.toDto()
//                }
//            }.awaitAll().filterNotNull()
//        } else emptyList()
//
//        val allResults = exactResults + similarMatches
//
//        // Cache results
//        cacheService.setSongsById(allResults)
//        cacheService.setSongsByTitle(allResults)
//        cacheService.setBatchSongs(titles, allResults)
//
//        allResults
//    }
//
//    /**
//     * Single title search with priority: Exact > StartsWith > Contains
//     * Returns results ordered by match quality
//     */
//    @Transactional(readOnly = true)
//    suspend fun searchSongByTitle(
//        searchTerm: String,
//        limit: Int = 20,
//    ): List<DtoSong> = withContext(Dispatchers.IO) {
//        songRepository.findByTitleExactOrSimilarWithFilters(searchTerm, limit)
//            .map { it.toDto() }
//            .also { songs ->
//                cacheService.setSongsById(songs)
//            }
//    }
//
//    /**
//     * Advanced fuzzy search with filters - for autocomplete/search
//     * Uses REGEXP for flexible matching + quality filters
//     */
//    @Transactional(readOnly = true)
//    suspend fun searchSongsByTitleWithFilters(
//        searchTerm: String,
//        page: Int = 0,
//        size: Int = 20,
//    ): List<DtoSong> = withContext(Dispatchers.IO) {
//        // Check cache first
//        cacheService.getSearchResults(searchTerm, page, size)?.let { return@withContext it }
//
//        // Build regex pattern for fuzzy matching
//        // "bohemian" -> ".*bohemian.*" (matches anywhere in title)
//        val pattern = ".*${searchTerm.lowercase()}.*"
//
//        val results = songRepository.findBySimilarTitleWithFilters(pattern, size)
//            .map { it.toDto() }
//            .also { songs ->
//                // Cache results
//                cacheService.setSearchResults(searchTerm, page, size, songs)
//                // Also cache individual songs
//                cacheService.setSongsById(songs)
//            }
//
//        results
//    }
//
//    /**
//     * Get songs with popularity ranking - for recommendations
//     */
//    @Transactional(readOnly = true)
//    suspend fun searchSongsOrderedByPopularity(
//        searchTerm: String,
//        page: Int = 0,
//        size: Int = 20,
//    ): List<DtoSong> = withContext(Dispatchers.IO) {
//        val pageable = PageRequest.of(page, size)
//
//        songRepository.searchByTitleOrderByPopularity(searchTerm, pageable)
//            .map { it.toDto() }
//            .also { songs ->
//                cacheService.setSongsById(songs)
//            }
//    }
//
//    /**
//     * Get songs with their info in single query (no N+1 problem)
//     */
//    @Transactional(readOnly = true)
//    suspend fun getSongsWithInfo(ids: List<SongId>): List<DtoSong> = coroutineScope {
//        if (ids.isEmpty()) return@coroutineScope emptyList()
//
//        // Check cache first (multi-get)
//        val cachedSongs = cacheService.getSongsByIds(ids)
//        val uncachedIds = ids.filter { it !in cachedSongs.keys }
//
//        // Fetch uncached from DB with JOIN FETCH (single query!)
//        val dbSongs = if (uncachedIds.isNotEmpty()) {
//            async(Dispatchers.IO) {
//                songRepository.findAllByIdsWithInfo(uncachedIds)
//                    .map { it.toDto() }
//                    .also { songs ->
//                        cacheService.setSongsById(songs)
//                    }
//            }.await()
//        } else emptyList()
//
//        // Combine and maintain original order
//        val allSongsMap = (cachedSongs.values + dbSongs).associateBy { it.id }
//        ids.mapNotNull { allSongsMap[it] }
//    }
//
//    /**
//     * Prefix search - for autocomplete (fast!)
//     */
//    @Transactional(readOnly = true)
//    suspend fun getSongsByTitlePrefix(
//        prefix: String,
//        limit: Int = 10,
//    ): List<DtoSong> = withContext(Dispatchers.IO) {
//        val pageable = PageRequest.of(0, limit)
//
//        songRepository.findByTitleStartingWith(prefix, pageable)
//            .map { it.toDto() }
//            .also { songs ->
//                cacheService.setSongsById(songs)
//            }
//    }
//
//    // ============= Helper Functions =============
//
//    /**
//     * Filter out unwanted song versions
//     * Replaces complex filtering in old Exposed code
//     */
//    private fun isQualitySong(title: String): Boolean {
//        val unwantedKeywords = listOf(
//            "remix", "mashup", "lofi", "slowed",
//            "new years", "reverb", "8d audio", "nightcore"
//        )
//        val lowerTitle = title.lowercase()
//        return unwantedKeywords.none { lowerTitle.contains(it) }
//    }
//
//    /**
//     * Extension function to convert Entity to DTO
//     * TODO: Implement based on your actual DtoSong structure
//     */
//    private fun EntitySong.toDto(): DtoSong {
//        return DtoSong(
//            id = this.id,
//            title = this.title,
//            poster = this.poster,
//            url = this.url,
//            // Map songInfo if available
//            releaseYear = this.songInfo?.releaseYear,
//            composer = this.songInfo?.composer,
//            popularity = this.songInfo?.popularity ?: 0
//        )
//    }
//}
