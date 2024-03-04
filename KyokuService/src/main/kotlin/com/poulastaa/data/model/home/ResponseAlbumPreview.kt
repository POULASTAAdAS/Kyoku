package com.poulastaa.data.model.home

import kotlinx.serialization.Serializable

@Serializable
data class ResponseAlbumPreview(
    val listOfPreviewAlbum: List<AlbumPreview> = emptyList()
)
