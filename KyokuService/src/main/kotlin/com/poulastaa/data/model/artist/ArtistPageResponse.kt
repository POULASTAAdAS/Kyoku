package com.poulastaa.data.model.artist

import com.poulastaa.data.model.home.AlbumPreview
import com.poulastaa.data.model.home.HomeResponseStatus
import com.poulastaa.data.model.home.SongPreview
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
    val coverImage: String = "",
    val year: String = ""
)
