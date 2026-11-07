package com.poulastaa.kyoku.playlist.domain.model.internal

import com.google.gson.annotations.SerializedName

/**
 * Spotify `GET /v1/playlists/{id}/tracks` page.
 */
data class PlaylistResponse(
    @SerializedName("items")
    val items: List<PlaylistItem>? = null,
    @SerializedName("next")
    val next: String? = null,
)
