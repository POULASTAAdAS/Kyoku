package com.poulastaa.data.model.auth.auth_response

import kotlinx.serialization.Serializable

@Serializable
data class ResponseAlbumPreview(
    val listOfPreviewAlbum: List<AlbumPreview> = emptyList()
)
