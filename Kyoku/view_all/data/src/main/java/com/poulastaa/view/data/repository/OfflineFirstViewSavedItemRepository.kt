package com.poulastaa.view.data.repository

import com.poulastaa.core.domain.repository.LocalViewSavedItemDatasource
import com.poulastaa.view.data.mapper.toDtoViewSavedItem
import com.poulastaa.view.domain.model.DtoViewSavedItem
import com.poulastaa.view.domain.model.DtoViewSavedItemType
import com.poulastaa.view.domain.repository.RemoteViewSavedItemDatasource
import com.poulastaa.view.domain.repository.ViewSavedItemRepository
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

internal class OfflineFirstViewSavedItemRepository @Inject constructor(
    private val remote: RemoteViewSavedItemDatasource,
    private val local: LocalViewSavedItemDatasource,
    private val scope: CoroutineScope,
) : ViewSavedItemRepository {
    override suspend fun loadSavedData(type: DtoViewSavedItemType): List<DtoViewSavedItem> =
        when (type) {
            DtoViewSavedItemType.ARTIST -> local.getSavedArtist().map { it.toDtoViewSavedItem() }
            DtoViewSavedItemType.PLAYLIST -> local.getSavedPlaylist()
                .map { it.toDtoViewSavedItem() }

            DtoViewSavedItemType.ALBUM -> local.getSavedAlbum().map { it.toDtoViewSavedItem() }
        }
}