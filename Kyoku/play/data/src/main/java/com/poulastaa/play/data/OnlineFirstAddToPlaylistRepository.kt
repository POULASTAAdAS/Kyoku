package com.poulastaa.play.data

import com.poulastaa.core.domain.add_to_playlist.AddToPlaylistRepository
import com.poulastaa.core.domain.add_to_playlist.LocalAddToPlaylistDatasource
import com.poulastaa.core.domain.add_to_playlist.RemoteAddToPlaylistDatasource
import com.poulastaa.core.domain.model.PrevSavedPlaylist
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OnlineFirstAddToPlaylistRepository @Inject constructor(
    private val local: LocalAddToPlaylistDatasource,
    private val remote: RemoteAddToPlaylistDatasource,
    private val application: CoroutineScope
): AddToPlaylistRepository {
    override suspend fun checkIfSongInFev(songId: Long): Boolean = local.checkIfSongInFev(songId)

    override suspend fun getTotalSongsInFev(): Int  = local.getTotalSongsInFev()

    override suspend fun getPlaylistData(): Flow<List<Pair<Int, PrevSavedPlaylist>>> = local.getPlaylistData()

    override suspend fun addSongToPlaylist(songId: Long, playlistId: Long) {

    }

    override suspend fun addSongToFavourite(songId: Long) {

    }
}