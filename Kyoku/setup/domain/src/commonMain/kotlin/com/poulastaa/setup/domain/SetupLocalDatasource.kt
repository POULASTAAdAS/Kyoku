package com.poulastaa.setup.domain

import com.poulastaa.common.domain.model.DtoFullPlaylist
import com.poulastaa.common.domain.result.AppResult
import com.poulastaa.common.domain.result.DataError
import com.poulastaa.common.domain.result.EmptyResponse

interface SetupLocalDatasource {
    suspend fun isSpotifyPlaylistImported(spotifyPlaylistId: String): AppResult<Boolean, DataError>
    suspend fun savePlaylist(playlist: DtoFullPlaylist, spotifyPlaylistId: String): EmptyResponse<DataError>
    suspend fun getPlaylists(): AppResult<List<DtoFullPlaylist>, DataError>

    /** Replaces every stored playlist with [playlists]; playlists missing from it are removed. */
    suspend fun replacePlaylists(playlists: List<DtoFullPlaylist>): EmptyResponse<DataError>
}
