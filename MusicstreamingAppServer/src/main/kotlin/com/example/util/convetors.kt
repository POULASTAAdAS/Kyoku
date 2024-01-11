package com.example.util

import com.example.data.model.EndPoints
import com.example.data.model.Payload
import com.example.data.model.ResponseSong
import com.example.domain.dao.Song
import com.example.domain.repository.jwt.JWTRepository
import com.example.util.Constants.BASE_URL
import com.example.util.Constants.COVER_IMAGE_ROOT_DIR
import com.example.util.Constants.MASTER_PLAYLIST_ROOT_DIR
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken

fun Song.toResponseSong(): ResponseSong = ResponseSong(
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

fun GoogleIdToken.toPayload() = Payload(
    sub = this.payload["sub"].toString(),
    userName = this.payload["name"].toString(),
    email = this.payload["email"].toString(),
    pictureUrl = this.payload["picture"].toString()
)


fun String.getAlbum(): String {
    val temp = Regex("\"([^\"]+)\"").find(this)

    temp?.let {
        return it.groupValues[1].trim()
    }
    return this.replace(Regex("\\(.*"), "").trim()
}

fun String.removeAlbum(): String =
    this.replace(Regex("\\(.*"), "").trim()

fun constructProfileUrl(): String = "$BASE_URL${EndPoints.ProfilePic.route}"

fun String.constructCoverPhotoUrl(): String = "$BASE_URL${EndPoints.CoverImage.route}?coverImage=${
    this.replace(COVER_IMAGE_ROOT_DIR, "")
}"

fun String.constructMasterPlaylistUrl(): String =
    "$BASE_URL${EndPoints.PlaySongMaster.route}?playlist=${
        this.replace(MASTER_PLAYLIST_ROOT_DIR, "")
    }"
