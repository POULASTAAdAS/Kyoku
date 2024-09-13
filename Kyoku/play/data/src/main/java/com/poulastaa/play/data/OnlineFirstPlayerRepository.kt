package com.poulastaa.play.data

import android.content.Context
import com.poulastaa.core.domain.PlayType
import com.poulastaa.core.domain.PlayerInfo
import com.poulastaa.core.domain.model.PlayerSong
import com.poulastaa.core.domain.repository.player.LocalPlayerDatasource
import com.poulastaa.core.domain.repository.player.PlayerRepository
import com.poulastaa.core.domain.utils.DataError
import com.poulastaa.core.domain.utils.EmptyResult
import com.poulastaa.core.domain.utils.Result
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OnlineFirstPlayerRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val local: LocalPlayerDatasource
) : PlayerRepository {
    override suspend fun loadData(
        id: Long,
        type: PlayType
    ): EmptyResult<DataError.Network> = coroutineScope {
        async { local.clearAll() }.await()

        when (type) {
            PlayType.PLAYLIST -> {
                when (local.isPlaylistOrAlbumSaved(id, true)) {
                    true -> {
                        val playlistDef = async { local.getPlaylist(id) }
                        val songsDef = async { local.getPlaylistSongs(id) }


                        val playlist = playlistDef.await()
                        val songs = songsDef.await()

                        Result.Success(
                            local.loadData(
                                songs = songs,
                                id = playlist.id,
                                name = playlist.name
                            )
                        )
                    }

                    false -> Result.Error(DataError.Network.NOT_FOUND)
                }
            }

            PlayType.ALBUM -> {
                when (local.isPlaylistOrAlbumSaved(id, false)) {
                    true -> {
                        coroutineScope {
                            val playlistDef = async { local.getAlbum(id) }
                            val songsDef = async { local.getAlbumSongs(id) }

                            val album = playlistDef.await()
                            val songs = songsDef.await()

                            Result.Success(
                                local.loadData(
                                    songs = songs,
                                    id = album.albumId,
                                    name = album.name
                                )
                            )
                        }
                    }

                    false -> Result.Error(DataError.Network.NOT_FOUND)
                }
            }

            PlayType.FEV -> Result.Success(local.loadFev())
            PlayType.OLD_MIX -> Result.Success(local.loadOldMix())
            PlayType.ARTIST_MIX -> Result.Success(local.loadArtistMix())
            PlayType.POPULAR_MIX -> Result.Success(local.loadPopularArtistMix())

            else -> Result.Error(DataError.Network.NOT_FOUND)
        }
    }

    override fun getInfo(): Flow<PlayerInfo> = local.getInfo()

    override fun getSongs(): Flow<List<PlayerSong>> = local.getSongs()
}