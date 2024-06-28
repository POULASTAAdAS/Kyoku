package com.poulastaa.core.domain.get_spotify_playlist

import com.poulastaa.core.domain.model.PlaylistWithSongInfo
import com.poulastaa.core.domain.utils.DataError
import com.poulastaa.core.domain.utils.EmptyResult
import kotlinx.coroutines.flow.Flow

interface SpotifyRepository {
    fun getPlaylists(): Flow<List<PlaylistWithSongInfo>>
    suspend fun insertPlaylist(url: String): EmptyResult<DataError>

    suspend fun updateCoverImage(
        songId: Long,
        encodedString: String,
    )
}