package com.poulastaa.play.data

import com.poulastaa.core.domain.add_to_playlist.AddToPlaylistRepository
import com.poulastaa.core.domain.add_to_playlist.LocalAddToPlaylistDatasource
import com.poulastaa.core.domain.add_to_playlist.PRESENT
import com.poulastaa.core.domain.add_to_playlist.RemoteAddToPlaylistDatasource
import com.poulastaa.core.domain.add_to_playlist.SIZE
import com.poulastaa.core.domain.model.PrevSavedPlaylist
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import javax.inject.Inject

class OnlineFirstAddToPlaylistRepository @Inject constructor(
    private val local: LocalAddToPlaylistDatasource,
    private val remote: RemoteAddToPlaylistDatasource,
    private val application: CoroutineScope
) : AddToPlaylistRepository {
    override suspend fun checkIfSongInFev(songId: Long): Boolean =
        application.async { local.checkIfSongInFev(songId) }.await()

    override suspend fun getTotalSongsInFev(): Int =
        application.async { local.getTotalSongsInFev() }.await()

    override suspend fun getPlaylistData(songId: Long): List<Pair<Pair<SIZE, PRESENT>, PrevSavedPlaylist>> =
        application.async { local.getPlaylistData(songId) }.await()

    override suspend fun addSongToPlaylist(songId: Long, playlistId: Long) {

    }

    override suspend fun addSongToFavourite(songId: Long) {

    }
}