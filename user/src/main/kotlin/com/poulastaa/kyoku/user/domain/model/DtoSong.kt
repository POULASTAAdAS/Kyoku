package com.poulastaa.kyoku.user.domain.model

import com.poulastaa.kyoku.user.utils.SongId
import java.io.Serializable

/**
 * Data Transfer Object for Song
 * Implements Serializable for Redis caching
 */
data class DtoSong(
    val id: SongId,
    val title: String,
    val poster: String?,
    val url: String,

    // From EntitySongInfo (nullable if not loaded)
    val releaseYear: Int? = null,
    val composer: String? = null,
    val popularity: Long = 0,

    // Additional fields for rich responses
    val artists: List<String>? = null,
    val album: String? = null,
    val genres: List<String>? = null,
    val duration: Long? = null,
) : Serializable {
    companion object {
        private const val serialVersionUID = 1L
    }
}

/**
 * Lightweight DTO for search/autocomplete results
 */
data class DtoSongPreview(
    val id: SongId,
    val title: String,
    val poster: String?,
    val popularity: Long = 0,
) : Serializable {
    companion object {
        private const val serialVersionUID = 1L
    }
}

/**
 * Response for batch song queries
 */
data class DtoBatchSongResponse(
    val found: List<DtoSong>,
    val notFound: List<String>,
    val totalRequested: Int,
    val totalFound: Int,
) : Serializable {
    companion object {
        private const val serialVersionUID = 1L
    }
}
