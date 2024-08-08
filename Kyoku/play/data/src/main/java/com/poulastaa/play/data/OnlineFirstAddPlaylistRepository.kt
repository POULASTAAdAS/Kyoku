package com.poulastaa.play.data

import com.poulastaa.core.domain.AddPlaylistResType
import com.poulastaa.core.domain.ExploreType
import com.poulastaa.core.domain.add_playlist.AddPlaylistRepository
import com.poulastaa.core.domain.add_playlist.LocalAddPlaylistDatasource
import com.poulastaa.core.domain.add_playlist.RemoteAddPlaylistDatasource
import com.poulastaa.core.domain.model.PrevSong
import com.poulastaa.core.domain.utils.DataError
import com.poulastaa.core.domain.utils.Result
import com.poulastaa.core.domain.utils.map
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import javax.inject.Inject

class OnlineFirstAddPlaylistRepository @Inject constructor(
    private val local: LocalAddPlaylistDatasource,
    private val remote: RemoteAddPlaylistDatasource,
    private val application: CoroutineScope,
) : AddPlaylistRepository {
    override suspend fun getSongId(type: ExploreType): List<PrevSong> = local.getSongId(type)

    override suspend fun savePlaylist(
        idList: List<Long>,
        name: String,
        type: ExploreType,
    ): Result<AddPlaylistResType, DataError.Network> = application.async {
        if (local.isNameConflict(name)) return@async Result.Success(AddPlaylistResType.NAME_CONFLICT)

        val result = remote.savePlaylist(idList, name, type)

        result.map {
            if (it.id == -1L) AddPlaylistResType.FAILED
            else {
                local.savePlaylist(it)
                AddPlaylistResType.SAVED
            }
        }
    }.await()
}