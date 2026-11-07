package com.poulastaa.setup.viewmodels.import_playlist

import com.poulastaa.common.domain.model.DtoFullPlaylist
import com.poulastaa.common.domain.model.DtoSong

private const val MAX_PLAYLIST_POSTERS = 4

internal fun DtoFullPlaylist.toUiPlaylist() = UiPlaylist(
    id = playlist.id,
    title = playlist.name,
    posters = songs.mapNotNull { it.poster }.take(MAX_PLAYLIST_POSTERS),
    tracks = songs.map { it.toUiImportPlaylistTrack() },
)

private fun DtoSong.toUiImportPlaylistTrack() = UiImportPlaylistTrack(
    id = id,
    title = title,
    artist = artists.map { it.name },
    poster = poster,
)
