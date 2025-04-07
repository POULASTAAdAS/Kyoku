package com.poulastaa.view.domain.repository

import com.poulastaa.view.domain.model.DtoViewSavedItem
import com.poulastaa.view.domain.model.DtoViewSavedItemType

interface ViewSavedItemRepository {
    suspend fun loadSavedData(type: DtoViewSavedItemType): List<DtoViewSavedItem>
}