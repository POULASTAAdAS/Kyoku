package com.poulastaa.data.model.db_table

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.Column

object SongTable : LongIdTable() {
    val coverImage: Column<String> = text("coverImage")
    val masterPlaylistPath: Column<String> = text("masterPlaylistPath")
    val totalTime: Column<String> = text("totalTime")
    val title: Column<String> = text("title")
    val artist: Column<String> = text("artist")
    val album: Column<String> = text("album")
    val genre: Column<String> = text("genre")
    val composer: Column<String> = text("composer")
    val publisher: Column<String> = text("publisher")
    val album_artist: Column<String> = text("album_artist")
    val description: Column<String> = text("description")
    val track = text("track")
    val date: Column<String> = text("date")
    val points = long("points").default(0)
}