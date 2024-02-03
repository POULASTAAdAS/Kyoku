package com.poulastaa.data.model.api.spotify


data class HandleSpotifyPlaylist(
    val spotifyPlaylistResponse: SpotifyPlaylistResponse = SpotifyPlaylistResponse(),
    val spotifySongDownloaderApiReq: SpotifySongDownloaderApiReq = SpotifySongDownloaderApiReq(),
    val status: HandleSpotifyPlaylistStatus
)
