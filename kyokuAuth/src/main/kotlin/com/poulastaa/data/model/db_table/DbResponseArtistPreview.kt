
package com.poulastaa.data.model.db_table

import com.poulastaa.data.model.auth.auth_response.SongPreview

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
