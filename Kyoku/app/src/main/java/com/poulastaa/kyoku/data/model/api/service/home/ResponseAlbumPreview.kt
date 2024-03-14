package com.poulastaa.kyoku.data.model.api.service.home

import kotlinx.serialization.Serializable

@Serializable
data class ResponseAlbumPreview(
    val listOfPreviewAlbum: List<AlbumPreview> = emptyList()
)
