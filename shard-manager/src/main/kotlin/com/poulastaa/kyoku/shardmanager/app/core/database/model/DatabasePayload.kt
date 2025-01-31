package com.poulastaa.kyoku.shardmanager.app.core.database.model

data class DatabasePayload(
    val driverClassName: String,
    val kyokuUrl: String,
    val kyokuUserUrl: String,
    val shardGenreArtistUrl: String,
    val shardPopularSongUrl: String,
)