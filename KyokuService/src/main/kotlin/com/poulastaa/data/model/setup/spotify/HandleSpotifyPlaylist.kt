package com.poulastaa.data.model.setup.spotify

data class HandleSpotifyPlaylist(
    val spotifyPlaylistResponse: SpotifyPlaylistResponse = SpotifyPlaylistResponse(),
    val spotifySongDownloaderApiReq: SpotifySongDownloaderApiReq = SpotifySongDownloaderApiReq(),
    val status: HandleSpotifyPlaylistStatus,
    val songIdList: List<Long> = emptyList()
)
