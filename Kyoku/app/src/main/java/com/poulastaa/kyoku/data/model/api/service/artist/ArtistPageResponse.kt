package com.poulastaa.kyoku.data.model.api.service.artist

import com.poulastaa.kyoku.data.model.api.service.home.HomeResponseStatus
import com.poulastaa.kyoku.data.model.api.service.home.SongPreview
import kotlinx.serialization.Serializable

@Serializable
data class ArtistPageResponse(
    val status: HomeResponseStatus = HomeResponseStatus.FAILURE,
    val albums: List<ArtistAlbum> = emptyList(),
    val songs: List<SongPreview> = emptyList()
)

@Serializable
data class ArtistAlbum(
    val id: Long = 0,
    val name: String = "",
    val coverImage: String = ""
)
