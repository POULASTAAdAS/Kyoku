package com.poulastaa.data.model.db_table

import com.poulastaa.data.model.auth.auth_response.ResponseSong

data class AlbumResponse(
    val id: Long,
    val name: String,
    val song: ResponseSong
)
