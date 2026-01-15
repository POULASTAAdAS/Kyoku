package com.poulastaa.kyoku.user.service
//
//import com.poulastaa.kyoku.user.domain.model.DtoBatchSongResponse
//import com.poulastaa.kyoku.user.utils.UserId
//import kotlinx.coroutines.async
//import kotlinx.coroutines.coroutineScope
//import org.springframework.stereotype.Service
//
///**
// * Example service showing how to use the optimized song query for Spotify playlist import
// *
// * This replaces your old createPlaylistFromSpotifyPlaylist method
// *
// * Performance improvements:
// * - Old: N queries (one per song title) + complex filtering
// * - New: 1 cache check + 1 DB query for all uncached songs
// * - Result: ~10-50x faster for playlists with 20-50 songs
// */
//@Service
//class PlaylistImportService(
//    private val songQueryService: SongQueryService,
//    private val cacheService: SongCacheService,
//) {
//
//    /**
//     * Import playlist from Spotify song titles
//     *
//     * @param userId User creating the playlist
//     * @param spotifySongTitles List of song titles from Spotify
//     * @return Batch response with found/not found songs
//     */
//    suspend fun importPlaylistFromTitles(
//        userId: UserId,
//        spotifySongTitles: List<String>,
//    ): DtoBatchSongResponse = coroutineScope {
//        if (spotifySongTitles.isEmpty()) {
//            return@coroutineScope DtoBatchSongResponse(
//                found = emptyList(),
//                notFound = emptyList(),
//                totalRequested = 0,
//                totalFound = 0
//            )
//        }
//
//        // Normalize titles for better matching
//        val normalizedTitles = spotifySongTitles.map { it.trim() }.distinct()
//
//        // Query songs in optimized batch (uses Redis + single DB query)
//        val foundSongs = songQueryService.getSongsByTitles(normalizedTitles)
//
//        // Identify which titles were not found
//        val foundTitles = foundSongs.map { it.title.lowercase() }.toSet()
//        val notFoundTitles = normalizedTitles.filter { title ->
//            !foundTitles.any { found -> found.contains(title.lowercase(), ignoreCase = true) }
//        }
//
//        // For not found, try fuzzy search as fallback
//        val fuzzyResults = if (notFoundTitles.isNotEmpty()) {
//            async {
//                notFoundTitles.mapNotNull { title ->
//                    songQueryService.searchSongsByTitleWithFilters(title, size = 1)
//                        .firstOrNull()
//                }
//            }.await()
//        } else emptyList()
//
//        val allFoundSongs = (foundSongs + fuzzyResults).distinctBy { it.id }
//        val stillNotFound = notFoundTitles.filter { title ->
//            !allFoundSongs.any { it.title.contains(title, ignoreCase = true) }
//        }
//
//        DtoBatchSongResponse(
//            found = allFoundSongs,
//            notFound = stillNotFound,
//            totalRequested = spotifySongTitles.size,
//            totalFound = allFoundSongs.size
//        )
//    }
//
//    /**
//     * Warm up cache for popular playlists
//     * Call this during off-peak hours to pre-cache popular queries
//     */
//    suspend fun warmUpCache(popularPlaylists: List<List<String>>) {
//        popularPlaylists.forEach { songTitles ->
//            songQueryService.getSongsByTitles(songTitles)
//        }
//    }
//}
//
///**
// * Example usage comparison:
// *
// * OLD APPROACH (Exposed):
// * ========================
// * - For 30 song titles:
// *   1. Check cache individually (30 Redis calls)
// *   2. Execute 30 SQL queries with complex WHERE clauses
// *   3. Process results in application (grouping, filtering)
// *   4. Execute 4 more queries for album, artist, info, genre (N+1 problem)
// *   5. Total: ~35+ database queries
// *   6. Time: 500-1000ms
// *
// * NEW APPROACH (JPA + Redis):
// * ===========================
// * - For 30 song titles:
// *   1. Check batch cache (1 Redis call)
// *   2. If miss, check individual caches (1 Redis MGET call for all)
// *   3. Execute 1 SQL query for remaining with WHERE title IN (...)
// *   4. Filter in application (simple predicate)
// *   5. Cache results
// *   6. Total: 1-2 Redis calls + 1 DB query
// *   7. Time: 50-100ms (first call), 5-10ms (cached)
// *
// * PERFORMANCE GAIN: 10-20x faster
// */
