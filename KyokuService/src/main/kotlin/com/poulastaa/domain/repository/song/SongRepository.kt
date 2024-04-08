package com.poulastaa.domain.repository.song

import com.poulastaa.data.model.common.ResponseSong
import com.poulastaa.data.model.home.DailyMixPreview
import com.poulastaa.data.model.pinned.PinnedReq
import com.poulastaa.data.model.setup.spotify.HandleSpotifyPlaylist
import com.poulastaa.data.model.setup.spotify.SpotifySong
import com.poulastaa.data.model.utils.UserType
import com.poulastaa.data.model.utils.UserTypeHelper

interface SongRepository {
    enum class FavouriteOperation {
        ADD,
        REMOVE
    }

    suspend fun handleSpotifyPlaylist(list: List<SpotifySong>): HandleSpotifyPlaylist
    suspend fun getDailyMixPreview(helper: UserTypeHelper): DailyMixPreview
    suspend fun getDailyMix(helper: UserTypeHelper): DailyMixPreview

    suspend fun handlePinnedOperation(
        userId: Long,
        userType: UserType,
        req: PinnedReq
    ): Boolean

    suspend fun getResponseSongOnId(listOfId: List<Long>): List<ResponseSong>

    suspend fun getResponseSong(songId: Long): ResponseSong

    suspend fun handleFavouriteOperation(
        userType: UserType,
        userId: Long,
        songId: Long,
        operation: FavouriteOperation
    )
}