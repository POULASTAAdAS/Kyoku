package com.poulastaa.domain.repository.aritst

import com.poulastaa.data.model.home.FevArtistsMixPreview
import com.poulastaa.data.model.home.ResponseArtistsPreview
import com.poulastaa.data.model.setup.artist.StoreArtistResponse
import com.poulastaa.data.model.setup.artist.SuggestArtistReq
import com.poulastaa.data.model.setup.artist.SuggestArtistResponse
import com.poulastaa.data.model.utils.UserType

interface ArtistRepository {
    suspend fun suggestArtist(
        req: SuggestArtistReq,
        countryId: Int
    ): SuggestArtistResponse

    suspend fun storeArtist(
         usedId:Long,
        userType: UserType,
        artistNameList: List<String>
    ): StoreArtistResponse

    suspend fun getArtistMixPreview(
        usedId:Long,
        userType: UserType
    ): List<FevArtistsMixPreview>

    suspend fun getResponseArtistPreviewForNewUser(
        usedId:Long,
        userType: UserType
    ): List<ResponseArtistsPreview>

    suspend fun getResponseArtistPreviewDailyUser(
        usedId:Long,
        userType: UserType
    ): List<ResponseArtistsPreview>
}