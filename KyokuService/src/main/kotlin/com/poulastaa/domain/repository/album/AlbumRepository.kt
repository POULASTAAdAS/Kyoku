package com.poulastaa.domain.repository.album

import com.poulastaa.data.model.home.ResponseAlbumPreview
import com.poulastaa.data.model.utils.UserType

interface AlbumRepository {
    suspend fun getResponseAlbumPreviewForNewUser(artistIdList: List<Int>): ResponseAlbumPreview
    suspend fun getResponseAlbumPreviewForDailyRefresh(
        userType: UserType,
        userId: Long
    ): ResponseAlbumPreview
}