package com.poulastaa.domain.repository.song

import com.poulastaa.data.model.setup.spotify.HandleSpotifyPlaylist
import com.poulastaa.data.model.setup.spotify.SpotifySong
import java.io.File

interface SongRepository {
    suspend fun handleSpotifyPlaylist(list: List<SpotifySong>): HandleSpotifyPlaylist
}