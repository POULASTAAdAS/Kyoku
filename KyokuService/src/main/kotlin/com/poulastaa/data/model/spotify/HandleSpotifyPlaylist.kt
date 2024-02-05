package com.poulastaa.data.model.spotify

data class HandleSpotifyPlaylist(
    val spotifyPlaylistResponse: SpotifyPlaylistResponse = SpotifyPlaylistResponse(),
    val spotifySongDownloaderApiReq: SpotifySongDownloaderApiReq = SpotifySongDownloaderApiReq(),
    val status: HandleSpotifyPlaylistStatus
)
