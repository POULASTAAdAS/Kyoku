package com.poulastaa.utils

import com.poulastaa.data.model.EndPoints
import com.poulastaa.data.model.spotify.ResponseSong
import com.poulastaa.domain.dao.Song
import com.poulastaa.utils.Constants.COVER_IMAGE_ROOT_DIR
import com.poulastaa.utils.Constants.BASE_URL
import com.poulastaa.utils.Constants.MASTER_PLAYLIST_ROOT_DIR

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

fun String.constructCoverPhotoUrl(): String = "$BASE_URL${EndPoints.CoverImage.route}?coverImage=${
    this.replace(COVER_IMAGE_ROOT_DIR, "")
}"

fun String.constructMasterPlaylistUrl(): String = "$BASE_URL${EndPoints.PlaySongMaster.route}?playlist=${
    this.replace(MASTER_PLAYLIST_ROOT_DIR, "")
}"