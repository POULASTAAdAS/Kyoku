package com.poulastaa.main.data.repository

import com.poulastaa.core.domain.repository.LocalLibraryDatasource
import com.poulastaa.main.data.mapper.toPayloadItem
import com.poulastaa.main.domain.model.PayloadSavedItem
import com.poulastaa.main.domain.repository.LibraryRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class OfflineFirstLibraryDatasource @Inject constructor(
    private val local: LocalLibraryDatasource,
//    private val remote:RemoteLibraryHomeDatasource,
    private val scope: CoroutineScope,
) : LibraryRepository {
    override suspend fun loadSavedPlaylist(): Flow<List<PayloadSavedItem>> =
        local.loadSavedPlaylist().map { list ->
            list.map { dto ->
                dto.toPayloadItem()
            }
        }

    override fun loadSavedArtist(): Flow<List<PayloadSavedItem>> =
        local.loadSavedArtist().map { list ->
            list.map { dto ->
                dto.toPayloadItem()
            }
        }

    override fun loadSavedAlbum(): Flow<List<PayloadSavedItem>> =
        local.loadSavedAlbum().map { list ->
            list.map { dto ->
                dto.toPayloadItem()
            }
        }
}