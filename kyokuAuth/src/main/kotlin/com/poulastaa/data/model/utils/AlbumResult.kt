package com.poulastaa.data.model.utils

data class AlbumResult(
    val albumId: Long = 0,
    val name: String,
    val albumPoints: Long,
    val songId: Long,
    val title: String,
    val artist: String,
    val cover: String,
    val points: Long,
    val year: String
)
