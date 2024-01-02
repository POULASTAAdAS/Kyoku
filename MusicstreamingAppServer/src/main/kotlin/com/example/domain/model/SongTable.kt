package com.example.domain.model

import org.jetbrains.exposed.sql.Table

object SongTable : Table() {
    val id = integer("id")
    val coverImage = text("coverImage")
    val masterPlaylistPath = text("masterPlaylistPath")
    val totalTime = text("totalTime")
    val title = text("title")
    val artist = text("artist")
    val album = text("album")
    val genre = text("genre")
    val composer = text("composer")
    val publisher = text("publisher")
    val album_artist = text("album_artist")
    val description = text("description")
    val track = text("track")
    val date = text("date")
}