package com.poulastaa.core.domain.repository.player

import com.poulastaa.core.domain.PlayerInfo
import com.poulastaa.core.domain.model.AlbumWithSong
import com.poulastaa.core.domain.model.PlayerSong
import com.poulastaa.core.domain.model.Playlist
import com.poulastaa.core.domain.model.PlaylistData
import com.poulastaa.core.domain.model.PrevAlbum
import com.poulastaa.core.domain.model.Song
import kotlinx.coroutines.flow.Flow

interface LocalPlayerDatasource {
    suspend fun clearAll()

    suspend fun isPlaylistOrAlbumSaved(id: Long, isPlaylist: Boolean): Boolean

    suspend fun getPlaylistSongs(id: Long): List<Song>
    suspend fun getPlaylist(id: Long): Playlist

    suspend fun getAlbumSongs(id: Long): List<Song>
    suspend fun getAlbum(id: Long): PrevAlbum

    suspend fun loadFev(isShuffled: Boolean = false)
    suspend fun loadOldMix(isShuffled: Boolean = false)
    suspend fun loadArtistMix(isShuffled: Boolean = false)
    suspend fun loadPopularArtistMix(isShuffled: Boolean = false)
    suspend fun loadData(songs: List<Song>, id: Long, name: String)

    suspend fun saveAlbum(payload: AlbumWithSong)
    suspend fun savePlaylist(payload: PlaylistData)

    fun getInfo(): Flow<PlayerInfo>
    fun getSongs(): Flow<List<PlayerSong>>
}