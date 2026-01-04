package com.poulastaa.kyoku.user.database.repository

import com.poulastaa.kyoku.user.database.entity.EntitySong
import com.poulastaa.kyoku.user.utils.SongId
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface SongRepository : JpaRepository<EntitySong, SongId> {
    /**
     * Find songs by exact title match (case-insensitive)
     * Best for: Exact lookups with caching
     */
    fun findByTitleIgnoreCase(title: String): EntitySong?

    /**
     * Find songs by title with LIKE pattern
     * Best for: Prefix search (e.g., "Bohemian%" for "Bohemian Rhapsody")
     */
    @Query("SELECT s FROM EntitySong s WHERE LOWER(s.title) LIKE LOWER(CONCAT(:prefix, '%'))")
    fun findByTitleStartingWith(@Param("prefix") prefix: String, pageable: Pageable): List<EntitySong>

    /**
     * Find songs by multiple titles in a single query (optimized batch lookup)
     * Best for: Spotify playlist import use case
     */
    @Query("SELECT s FROM EntitySong s WHERE LOWER(s.title) IN :titles")
    fun findAllByTitlesIgnoreCase(@Param("titles") titles: List<String>): List<EntitySong>

    /**
     * Find EXACT title matches with quality filters (no remixes, mashups, etc.)
     * Returns only original/quality versions of songs
     * Best for: Getting exact song when you know the title
     */
    @Query(
        value = """
            SELECT s.* FROM song s
            WHERE LOWER(s.title) = LOWER(:title)
            AND s.title NOT REGEXP '(Remix|Mashup|LoFi|Slowed|New Years|Reverb|8D|Nightcore)'
            LIMIT 1
        """,
        nativeQuery = true
    )
    fun findExactTitleWithFilters(@Param("title") title: String): EntitySong?

    /**
     * Find EXACT matches for multiple titles with quality filters (batch version)
     * Much more efficient than calling findExactTitleWithFilters multiple times
     * Best for: Spotify playlist import - getting exact original versions
     */
    @Query(
        value = """
            SELECT s.* FROM song s
            WHERE LOWER(s.title) IN (:titles)
            AND s.title NOT REGEXP '(Remix|Mashup|LoFi|Slowed|New Years|Reverb|8D|Nightcore)'
        """,
        nativeQuery = true
    )
    fun findExactTitlesWithFilters(@Param("titles") titles: List<String>): List<EntitySong>

    /**
     * Find SIMILAR title matches using REGEXP with quality filters
     * For fuzzy matching when exact match fails
     * Best for: Fallback when exact title doesn't match
     */
    @Query(
        value = """
            SELECT s.* FROM song s
            WHERE LOWER(s.title) REGEXP :pattern
            AND s.title NOT REGEXP '(Remix|Mashup|LoFi|Slowed|New Years|Reverb|8D|Nightcore)'
            LIMIT :limit
        """,
        nativeQuery = true
    )
    fun findBySimilarTitleWithFilters(
        @Param("pattern") pattern: String,
        @Param("limit") limit: Int,
    ): List<EntitySong>

    /**
     * Combined query: First try exact match, then similar matches
     * Returns exact match with highest priority, then similar ones
     * Best for: Smart search that prioritizes exact matches
     */
    @Query(
        value = """
            SELECT s.*, 
                   CASE 
                       WHEN LOWER(s.title) = LOWER(:searchTerm) THEN 1
                       WHEN LOWER(s.title) LIKE LOWER(CONCAT(:searchTerm, '%')) THEN 2
                       WHEN LOWER(s.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) THEN 3
                       ELSE 4
                   END as match_priority
            FROM song s
            WHERE (
                LOWER(s.title) = LOWER(:searchTerm)
                OR LOWER(s.title) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
            )
            AND s.title NOT REGEXP '(Remix|Mashup|LoFi|Slowed|New Years|Reverb|8D|Nightcore)'
            ORDER BY match_priority ASC, s.title ASC
            LIMIT :limit
        """,
        nativeQuery = true
    )
    fun findByTitleExactOrSimilarWithFilters(
        @Param("searchTerm") searchTerm: String,
        @Param("limit") limit: Int,
    ): List<EntitySong>

    /**
     * Fetch songs with their info in a single query using JOIN FETCH
     * Best for: Avoiding N+1 problem when you need song + info together
     */
    @Query(
        """
        SELECT DISTINCT s FROM EntitySong s 
        LEFT JOIN FETCH s.songInfo 
        WHERE s.id IN :ids
    """
    )
    fun findAllByIdsWithInfo(@Param("ids") ids: List<SongId>): List<EntitySong>

    /**
     * Optimized search with popularity ordering
     * Best for: Search results ranked by popularity
     */
    @Query(
        """
        SELECT s FROM EntitySong s 
        LEFT JOIN s.songInfo si
        WHERE LOWER(s.title) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
        ORDER BY si.popularity DESC
    """
    )
    fun searchByTitleOrderByPopularity(
        @Param("searchTerm") searchTerm: String,
        pageable: Pageable,
    ): List<EntitySong>
}
