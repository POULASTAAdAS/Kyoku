package com.poulastaa.data.model.setup.spotify

import com.poulastaa.domain.dao.Song
import com.poulastaa.domain.dao.playlist.Playlist

data class HandleSpotifyPlaylist(
    val status: HandleSpotifyPlaylistStatus,
    val playlist: Playlist? = null,
    val spotifyPlaylistResponse: SpotifyPlaylistResponse = SpotifyPlaylistResponse(),
    val spotifySongDownloaderApiReq: SpotifySongDownloaderApiReq = SpotifySongDownloaderApiReq(),
    val listOfSongs: List<Song> = emptyList()
)
