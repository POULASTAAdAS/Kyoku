package com.example.domain.repository.song_db

import com.example.data.model.HandleSpotifyPlaylist
import com.example.data.model.SpotifySong

interface SongRepository {
    suspend fun handleSpotifyPlaylist(list: List<SpotifySong>): HandleSpotifyPlaylist
}