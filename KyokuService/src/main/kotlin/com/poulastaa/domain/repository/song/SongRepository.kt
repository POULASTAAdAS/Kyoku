package com.poulastaa.domain.repository.song

import com.poulastaa.data.model.home.DailyMixPreview
import com.poulastaa.data.model.setup.spotify.HandleSpotifyPlaylist
import com.poulastaa.data.model.setup.spotify.SpotifySong
import com.poulastaa.data.model.utils.UserTypeHelper

interface SongRepository {
    suspend fun handleSpotifyPlaylist(list: List<SpotifySong>): HandleSpotifyPlaylist
    suspend fun getDailyMixPreview(helper: UserTypeHelper): DailyMixPreview
    suspend fun getDailyMix(helper: UserTypeHelper): DailyMixPreview
}