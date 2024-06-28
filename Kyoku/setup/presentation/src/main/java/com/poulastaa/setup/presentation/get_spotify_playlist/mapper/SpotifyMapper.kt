package com.poulastaa.setup.presentation.get_spotify_playlist.mapper

fun String.validateSpotifyLink() =
    (this.startsWith("https://open.spotify.com/playlist/") && this.contains("?si="))

fun String.toSpotifyPlaylistId(): String =
    this.removePrefix("https://open.spotify.com/playlist/").split("?si=")[0]