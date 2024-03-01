package com.poulastaa.domain.repository.album

import com.poulastaa.data.model.home.ResponseAlbumPreview

interface AlbumRepository {
    suspend fun getResponseAlbumPreview(artistIdList:List<Int>):List<ResponseAlbumPreview>
}