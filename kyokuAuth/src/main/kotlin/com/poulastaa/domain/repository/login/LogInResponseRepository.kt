package com.poulastaa.domain.repository.login

import com.poulastaa.data.model.UserType
import com.poulastaa.data.model.auth.auth_response.*

interface LogInResponseRepository {
    suspend fun getFevArtistMix(userId: Long,userType: UserType): List<FevArtistsMixPreview>
    suspend fun getAlbumPrev(userId: Long,userType: UserType): ResponseAlbumPreview
    suspend fun getArtistPrev(userId: Long,userType: UserType): List<ResponseArtistsPreview>
    suspend fun getDailyMixPrev(userId: Long,userType: UserType): DailyMixPreview
    suspend fun getHistoryPrev(userId: Long,userType: UserType): List<SongPreview>

    suspend fun getAlbums(userId: Long,userType: UserType): List<ResponseAlbum>
    suspend fun getPlaylists(userId: Long,userType: UserType): List<ResponsePlaylist>
    suspend fun getFavourites(userId: Long,userType: UserType): Favourites
}