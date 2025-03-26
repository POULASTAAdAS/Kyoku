package com.poulastaa.view.data.repository

import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.Result
import com.poulastaa.core.domain.map
import com.poulastaa.core.domain.model.DtoDetailedPrevSong
import com.poulastaa.core.domain.model.DtoViewPayload
import com.poulastaa.core.domain.model.ViewType
import com.poulastaa.core.domain.repository.LocalViewOtherDatasource
import com.poulastaa.view.data.mapper.toDtoDetailedPrevSong
import com.poulastaa.view.data.mapper.toDtoExploreType
import com.poulastaa.view.domain.repository.RemoteViewOtherDatasource
import com.poulastaa.view.domain.repository.ViewOtherRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class OnlineFirstViewOtherRepository @Inject constructor(
    private val local: LocalViewOtherDatasource,
    private val remote: RemoteViewOtherDatasource,
    private val scope: CoroutineScope,
) : ViewOtherRepository {
    override suspend fun getViewData(
        type: ViewType,
        otherId: Long,
    ): Result<DtoViewPayload<DtoDetailedPrevSong>, DataError.Network> {
        val savedData = local.getViewTypeData(type, otherId)

        return if (savedData == null || savedData.listOfSongs.isEmpty()) when (type) {
            ViewType.PLAYLIST,
            ViewType.FAVOURITE,
                -> {
                val remoteData =
                    if (type == ViewType.PLAYLIST) remote.getFavouriteOrPlaylistViewData(otherId)
                    else remote.getFavouriteOrPlaylistViewData()

                if (remoteData is Result.Success) scope.launch {
                    local.saveViewTypeData(
                        list = remoteData.data.listOfSongs,
                        playlistId = if (type == ViewType.PLAYLIST) otherId else null
                    )
                }

                remoteData.map { data ->
                    DtoViewPayload(
                        heading = data.heading,
                        listOfSongs = data.listOfSongs.map { it.toDtoDetailedPrevSong() }
                    )
                }
            }

            ViewType.ALBUM -> remote.getViewAlbum(type, otherId)

            ViewType.POPULAR_SONG_MIX,
            ViewType.POPULAR_YEAR_MIX,
            ViewType.SAVED_ARTIST_SONG_MIX,
            ViewType.DAY_TYPE_MIX,
                -> {
                val savedIdList = local.getSavedPrevViewSongIds(type)
                val remoteData = remote.getExploreViewData(type, savedIdList)

                if (remoteData is Result.Success) scope.launch {
                    local.saveViewTypeData(
                        type.toDtoExploreType(),
                        remoteData.data.listOfSongs
                    )
                }

                remoteData.map { data ->
                    DtoViewPayload(
                        heading = data.heading,
                        listOfSongs = data.listOfSongs.map { it.toDtoDetailedPrevSong() }
                    )
                }
            }

            else -> Result.Error(DataError.Network.UNKNOWN)
        } else Result.Success(savedData)
    }
}