package com.poulastaa.data.model.setup.spotify

import com.poulastaa.domain.dao.Song

data class HandleSpotifyPlaylist(
    val status: HandleSpotifyPlaylistStatus,
    val spotifyPlaylistResponse: SpotifyPlaylistResponse = SpotifyPlaylistResponse(),
    val spotifySongDownloaderApiReq: SpotifySongDownloaderApiReq = SpotifySongDownloaderApiReq(),
    val listOfSongs: List<Song> = emptyList()
)
