package com.poulastaa.domain.repository

import com.poulastaa.data.model.FindUserType
import com.poulastaa.data.model.UserType
import com.poulastaa.data.model.setup.set_b_date.SetBDateResponse
import com.poulastaa.data.model.spotify.SpotifyPlaylistResponse
import java.io.File

interface UserServiceRepository {
    suspend fun getFoundSpotifySongs(json: String, user: FindUserType): SpotifyPlaylistResponse
    suspend fun getSongCover(name: String): File?

    suspend fun storeBDate(date: Long, userType: UserType, id: String): SetBDateResponse
}