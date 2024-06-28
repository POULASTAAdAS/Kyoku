package com.poulastaa.setup.data

import com.poulastaa.core.data.BitmapConverter
import com.poulastaa.core.data.ColorGenerator
import com.poulastaa.core.domain.get_spotify_playlist.LocalSpotifyDataSource
import com.poulastaa.core.domain.get_spotify_playlist.RemoteSpotifyDataSource
import com.poulastaa.core.domain.get_spotify_playlist.SpotifyRepository
import com.poulastaa.core.domain.model.PlaylistWithSongInfo
import com.poulastaa.core.domain.utils.DataError
import com.poulastaa.core.domain.utils.EmptyResult
import com.poulastaa.core.domain.utils.Result
import com.poulastaa.core.domain.utils.asEmptyDataResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OnlineFirstSpotifyRepository @Inject constructor(
    private val local: LocalSpotifyDataSource,
    private val remote: RemoteSpotifyDataSource,
    private val applicationScope: CoroutineScope,
) : SpotifyRepository {
    private val defaultPrimary = "#002924"
    private val defaultBackground = "#111413"
    private val defaultOnBackground = "#739C93"

    override fun getPlaylists(): Flow<List<PlaylistWithSongInfo>> = local.getAllPlaylistWithSong()

    override suspend fun insertPlaylist(url: String): EmptyResult<DataError> {
        val result = remote.createPlaylist(url)

        if (result !is Result.Success) return result.asEmptyDataResult()
        if (result.data.songs.isEmpty()) return result.asEmptyDataResult()

        val sonIdfDef = applicationScope.async {
            local.insertSongs(result.data.songs)
        }

        val playlistIdDef = applicationScope.async {
            local.insertPlaylist(result.data.playlist)
        }

        return applicationScope.async {
            val songIdList = sonIdfDef.await()
            val playlistId = playlistIdDef.await()

            local.createRelationOnSongAndPlaylist(
                songIdList = songIdList,
                playlistId = playlistId
            )

            Result.Success(Unit)
        }.await()
    }

    override suspend fun updateCoverImage(
        songId: Long,
        encodedString: String,
    ) {
        applicationScope.async {
            val bitmap = BitmapConverter.decodeToBitmap(encodedString) ?: return@async

            val colorMap = ColorGenerator.extractColorFromBitMap(bitmap)

            var primary = colorMap[ColorGenerator.ColorType.LIGHT_MUTED]
            var background = colorMap[ColorGenerator.ColorType.DARK_MUTED]
            var onBackground = colorMap[ColorGenerator.ColorType.MUTED_SWATCH]

            if (primary == background) {
                primary = defaultPrimary
                background = defaultBackground
                onBackground = defaultOnBackground
            }

            local.addColorToSong(
                songId = songId,
                encodedCoverImage = encodedString,
                primary = primary ?: defaultPrimary,
                background = background ?: defaultBackground,
                onBackground = onBackground ?: defaultOnBackground
            )
        }.await()
    }
}