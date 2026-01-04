package com.poulastaa.kyoku.playlist.domain.model

import com.poulastaa.kyoku.playlist.utils.SongId

data class DtoSong(
    val id: SongId = 0,
    val title: String = "",
    val rawPoster: String? = null,
    val rawMasterPlaylist: String = "",
    val info: DtoSongInfo = DtoSongInfo(),
    val artists: List<DtoArtist> = emptyList(),
    val album: List<DtoAlbum> = emptyList(),
    val genre: List<DtoGenre> = emptyList(),
    val country: List<DtoCountry> = emptyList(),
) {
    // TODO: modify rawPoster and rawMasterPlaylist
    val poster = rawPoster
    val masterPlaylist = rawMasterPlaylist
}
