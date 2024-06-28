package com.poulastaa.setup.presentation.get_spotify_playlist.mapper

import com.poulastaa.core.domain.model.PlaylistSong
import com.poulastaa.core.domain.model.PlaylistWithSongInfo
import com.poulastaa.setup.presentation.get_spotify_playlist.model.UiPlaylist
import com.poulastaa.setup.presentation.get_spotify_playlist.model.UiSong

fun PlaylistSong.toUiSong() = UiSong(
    id = id,
    title = title,
    artist = artist,
    coverImage = coverImage
)


fun PlaylistWithSongInfo.toUiPlaylist() = UiPlaylist(
    id = playlistId,
    name = name,
    listOfUiSong = listOfPlaylistSong.map { it.toUiSong() }
)


fun String.validateSpotifyLink() =
    (this.startsWith("https://open.spotify.com/playlist/") && this.contains("?si="))

fun String.toSpotifyPlaylistId(): String =
    this.removePrefix("https://open.spotify.com/playlist/").split("?si=")[0]