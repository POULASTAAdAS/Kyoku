package com.poulastaa.kyoku.playlist.database.playlist.entity.ids

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import java.io.Serializable

@Embeddable
data class UserPlaylistId(
    @Column(name = "user_id", nullable = false)
    val userId: Long = 0,

    @Column(name = "playlist_id", nullable = false)
    val playlistId: Long = 0,
) : Serializable
