package com.poulastaa.domain.repository.song

import com.poulastaa.data.model.spotify.HandleSpotifyPlaylist
import com.poulastaa.data.model.spotify.SpotifySong
import java.io.File

interface SongRepository {
    suspend fun handleSpotifyPlaylist(list: List<SpotifySong>): HandleSpotifyPlaylist
}