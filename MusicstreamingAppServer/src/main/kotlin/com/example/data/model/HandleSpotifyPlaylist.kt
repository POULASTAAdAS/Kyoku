package com.example.data.model

import com.example.data.model.api.req.SpotifySongDownloaderApiReq
import com.example.data.model.api.res.SpotifyPlaylistResponse
import com.example.data.model.api.stat.HandleSpotifyPlaylistStatus

data class HandleSpotifyPlaylist(
    val spotifyPlaylistResponse: SpotifyPlaylistResponse = SpotifyPlaylistResponse(),
    val spotifySongDownloaderApiReq: SpotifySongDownloaderApiReq = SpotifySongDownloaderApiReq(),
    val status: HandleSpotifyPlaylistStatus
)
