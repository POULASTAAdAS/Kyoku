package com.example.data.model

data class HandleSpotifyPlaylist(
    val spotifyPlaylistResponse: SpotifyPlaylistResponse = SpotifyPlaylistResponse(),
    val spotifySongDownloaderApiReq: SpotifySongDownloaderApiReq = SpotifySongDownloaderApiReq(),
    val status: HandleSpotifyPlaylistStatus
)
