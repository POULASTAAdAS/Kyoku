package com.poulastaa.play.data

import android.content.Context
import com.poulastaa.core.domain.PlayType
import com.poulastaa.core.domain.PlayerInfo
import com.poulastaa.core.domain.model.ArtistWithPopularity
import com.poulastaa.core.domain.model.PlayerSong
import com.poulastaa.core.domain.model.SongOtherData
import com.poulastaa.core.domain.repository.player.LocalPlayerDatasource
import com.poulastaa.core.domain.repository.player.PlayerRepository
import com.poulastaa.core.domain.repository.player.RemotePlayerDatasource
import com.poulastaa.core.domain.repository.song_artist.RemoteSongArtistDatasource
import com.poulastaa.core.domain.utils.DataError
import com.poulastaa.core.domain.utils.EmptyResult
import com.poulastaa.core.domain.utils.Result
import com.poulastaa.core.domain.utils.asEmptyDataResult
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

class OnlineFirstPlayerRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val local: LocalPlayerDatasource,
    private val remote: RemotePlayerDatasource,
    private val artistRemote: RemoteSongArtistDatasource,
    private val applicationScope: CoroutineScope,
) : PlayerRepository {
    override suspend fun loadData(
        id: Long,
        type: PlayType,
        isShuffled: Boolean,
    ): EmptyResult<DataError.Network> = coroutineScope {
        async { local.clearAll() }.await()

        when (type) {
            PlayType.PLAYLIST -> {
                when (local.isPlaylistOrAlbumSaved(id, true)) {
                    true -> {
                        val playlistDef = async { local.getPlaylist(id) }
                        val songsDef = async {
                            local.getPlaylistSongs(id).let { songs ->
                                if (isShuffled) songs.shuffled(Random) else songs
                            }
                        }

                        val playlist = playlistDef.await()
                        val songs = songsDef.await()

                        applicationScope.launch {
                            local.loadData(
                                songs = songs,
                                id = playlist.id,
                                name = playlist.name
                            )
                        }.join()

                        Result.Success(Unit)
                    }

                    false -> {
                        val result = remote.getPlaylist(id)

                        if (result is Result.Success) applicationScope.launch {
                            local.loadData(
                                songs = if (isShuffled) result.data.listOfSong.shuffled(Random)
                                else result.data.listOfSong,
                                id = result.data.id,
                                name = result.data.name
                            )
                        }.join()

                        result.asEmptyDataResult()
                    }
                }
            }

            PlayType.ALBUM -> {
                when (local.isPlaylistOrAlbumSaved(id, false)) {
                    true -> {
                        coroutineScope {
                            val albumDef = async { local.getAlbum(id) }
                            val songsDef = async {
                                local.getAlbumSongs(id).let { songs ->
                                    if (isShuffled) songs.shuffled(Random) else songs
                                }
                            }

                            val album = albumDef.await()
                            val songs = songsDef.await()

                            applicationScope.launch {
                                local.loadData(
                                    songs = songs,
                                    id = album.albumId,
                                    name = album.name
                                )
                            }.join()

                            Result.Success(Unit)
                        }
                    }

                    false -> {
                        val result = remote.getAlbum(id)

                        if (result is Result.Success) applicationScope.launch {
                            local.loadData(
                                songs = if (isShuffled) result.data.listOfSong.shuffled(Random)
                                else result.data.listOfSong,
                                id = result.data.album.albumId,
                                name = result.data.album.name
                            )
                        }.join()

                        result.asEmptyDataResult()
                    }
                }
            }

            PlayType.FEV -> Result.Success(local.loadFev(isShuffled))
            PlayType.OLD_MIX -> Result.Success(local.loadOldMix(isShuffled))
            PlayType.ARTIST_MIX -> Result.Success(local.loadArtistMix(isShuffled))
            PlayType.POPULAR_MIX -> Result.Success(local.loadPopularArtistMix(isShuffled))

            else -> Result.Error(DataError.Network.NOT_FOUND)
        }
    }

    override fun getInfo(): Flow<PlayerInfo> = local.getInfo()
    override fun getSongs(): Flow<List<PlayerSong>> = local.getSongs()

    override suspend fun getOtherInfo(songId: Long): Result<SongOtherData, DataError.Network> =
        remote.loadOtherInfo(songId)

    override suspend fun getArtistOnSongId(songId: Long): Result<List<ArtistWithPopularity>, DataError.Network> =
        artistRemote.getArtistOnSongId(songId)

    override fun close() {
        applicationScope.launch {
            local.clearAll()
        }
    }
}