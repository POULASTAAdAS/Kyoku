package com.poulastaa.kyoku.shardmanager.app.core.database.model

data class DtoAlbum(
    val id: Long,
    val name: String,
    val poster: String?,
    val popularity: Long,
    val releaseYear: Int,
)
