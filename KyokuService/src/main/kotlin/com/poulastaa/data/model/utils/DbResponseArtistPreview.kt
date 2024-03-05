package com.poulastaa.data.model.utils

import com.poulastaa.data.model.home.SongPreview

data class DbResponseArtistPreview(
    val songId: String,
    val songTitle: String,
    val songCover: String,
    val artist: String,
    val album: String,
    val artistId: Int,
    val artistImage: String
) {
    companion object {
        fun List<DbResponseArtistPreview>.toListOfSongPreview() = this.map {
            SongPreview(
                id = it.songId,
                title = it.songTitle,
                coverImage = it.songCover,
                it.artist,
                album = it.album
            )
        }
    }
}
