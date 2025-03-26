package com.poulastaa.main.data.repository.work

import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.EmptyResult
import com.poulastaa.core.domain.Result
import com.poulastaa.core.domain.asEmptyDataResult
import com.poulastaa.core.domain.model.DtoRelationSuggestedArtistSong
import com.poulastaa.core.domain.repository.LocalRefreshDatasource
import com.poulastaa.main.domain.repository.work.RefreshRepository
import com.poulastaa.main.domain.repository.work.RemoteRefreshDatasource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import javax.inject.Inject

internal class DaggerHiltRefreshRepository @Inject constructor(
    private val local: LocalRefreshDatasource,
    private val remote: RemoteRefreshDatasource,
    private val scope: CoroutineScope,
) : RefreshRepository {
    override suspend fun refreshSuggestedData(): EmptyResult<DataError.Network> {
        val result = remote.refreshSuggestedData()

        if (result is Result.Success) {
            scope.launch {
                local.clearSuggestedData()

                val explore = listOf(
                    async { local.updatePopularSongMix(result.data.prevPopularSongMix) },
                    async { local.updatePopularArtistMix(result.data.prevPopularArtistMix) },
                    async { local.updateOldGem(result.data.prevOldGem) },
                )

                val other = listOf(
                    async { local.updateSuggestedArtist(result.data.suggestedArtist) },
                    async { local.updateSuggestedAlbum(result.data.suggestedAlbum) },
                )

                val savedSongs = result.data.suggestedArtistSong.map { it.prevSong }.flatten()
                val saveArtist = result.data.suggestedArtistSong.map { it.artist }

                val artistSongs = listOf(
                    async { local.storePrevSong(savedSongs) },
                    async { local.storeSuggestedArtist(saveArtist) },
                )

                explore.awaitAll()
                other.awaitAll()
                artistSongs.awaitAll()

                result.data.suggestedArtistSong.map { dto ->
                    DtoRelationSuggestedArtistSong(
                        artistId = dto.artist.id,
                        list = dto.prevSong.map { it.id }
                    )
                }.let {
                    local.updateRelationSuggestedArtistSong(it)
                }
            }
        }

        return result.asEmptyDataResult()
    }
}