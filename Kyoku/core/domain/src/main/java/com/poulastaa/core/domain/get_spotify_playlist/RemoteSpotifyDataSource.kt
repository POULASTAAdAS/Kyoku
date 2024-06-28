package com.poulastaa.core.domain.get_spotify_playlist

import com.poulastaa.core.domain.model.PlaylistWithSong
import com.poulastaa.core.domain.utils.DataError
import com.poulastaa.core.domain.utils.Result

interface RemoteSpotifyDataSource {
    suspend fun createPlaylist(url: String): Result<PlaylistWithSong, DataError.Network>
}