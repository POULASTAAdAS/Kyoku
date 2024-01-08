package com.example.util

import com.example.data.model.EndPoints
import com.example.data.model.ResponseSong
import com.example.domain.dao.Song

fun Song.toResponseSong(): ResponseSong {
    return ResponseSong(
        coverImage = this.coverImage.constructCoverPhotoUrl(),
        masterPlaylistUrl = this.masterPlaylistPath.constructMasterPlaylistUrl(),
        totalTime = this.totalTime,
        title = this.title,
        artist = this.artist,
        album = this.album,
        genre = this.genre,
        composer = this.composer,
        publisher = this.publisher,
        albumArtist = this.albumArtist,
        description = this.description,
        track = this.track,
        date = this.date
    )
}


fun String.getAlbum(): String {
    val temp = Regex("\"([^\"]+)\"").find(this)

    temp?.let {
        return it.groupValues[1].trim()
    }
    return this.replace(Regex("\\(.*"), "").trim()
}

fun String.removeAlbum(): String =
    this.replace(Regex("\\(.*"), "").trim()

fun constructProfileUrl(): String = "${Constants.BASE_URL}${EndPoints.ProfilePic.route}"

fun String.constructCoverPhotoUrl(): String = this

fun String.constructMasterPlaylistUrl(): String = this