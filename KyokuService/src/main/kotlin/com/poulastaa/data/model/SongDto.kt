package com.poulastaa.data.model

import com.poulastaa.domain.model.EndPoints
import com.poulastaa.utils.Constants.COVER_IMAGE_ROOT_DIR
import com.poulastaa.utils.Constants.MASTER_PLAYLIST_ROOT_DIR
import kotlinx.serialization.Serializable

@Serializable
data class SongDto(
    val id: Long = -1,
    val coverImage: String = "",
    val title: String = "",
    val artistName: String,
    val releaseYear: Int = -1,
    val masterPlaylistUrl: String = "",
    val lightVibrant: String = "",
    val darkVibrant: String = "",
    val mutedSwatch: String = "",
) {
    fun constructMasterPlaylistUrl() = "${System.getenv("SERVICE_URL") + EndPoints.PlaySongMaster.route}?master=${
        this.masterPlaylistUrl.replace(MASTER_PLAYLIST_ROOT_DIR, "")
    }"

    fun constructCoverImage() = "${System.getenv("SERVICE_URL") + EndPoints.PlaySongMaster.route}?coverImage=${
        this.coverImage.replace(COVER_IMAGE_ROOT_DIR, "")
    }"
}
