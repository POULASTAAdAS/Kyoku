package com.poulastaa.domain.repository.aritst

import com.poulastaa.data.model.home.FevArtistsMixPreview
import com.poulastaa.data.model.setup.artist.StoreArtistResponse
import com.poulastaa.data.model.setup.artist.SuggestArtistReq
import com.poulastaa.data.model.setup.artist.SuggestArtistResponse
import com.poulastaa.data.model.utils.UserTypeHelper

interface ArtistRepository {
    suspend fun suggestArtist(
        req: SuggestArtistReq,
        countryId: Int
    ): SuggestArtistResponse

    suspend fun storeArtist(
        helper: UserTypeHelper,
        artistNameList: List<String>
    ): StoreArtistResponse

    suspend fun getArtistMixPreview(
        helper: UserTypeHelper
    ):FevArtistsMixPreview
}