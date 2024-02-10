package com.poulastaa.domain.repository

import com.poulastaa.data.model.CreatePlaylistHelperUser
import com.poulastaa.data.model.spotify.SpotifyPlaylistResponse
import java.io.File

interface UserServiceRepository {
    suspend fun getFoundSpotifySongs(json: String, user: CreatePlaylistHelperUser): SpotifyPlaylistResponse
    suspend fun getSongCover(name: String): File?
}