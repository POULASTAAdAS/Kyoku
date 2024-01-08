package com.example.domain.repository.song_db

import com.example.data.model.HandleSpotifyPlaylist
import com.example.data.model.SpotifySong
import java.io.File

interface SongRepository {
    suspend fun handleSpotifyPlaylist(list: List<SpotifySong>): HandleSpotifyPlaylist
    fun getCoverImage(path: String): File?
}