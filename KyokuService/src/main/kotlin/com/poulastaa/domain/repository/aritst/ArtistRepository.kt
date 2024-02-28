package com.poulastaa.domain.repository.aritst

import com.poulastaa.data.model.UserTypeHelper
import com.poulastaa.data.model.setup.artist.StoreArtistResponse
import com.poulastaa.data.model.setup.artist.SuggestArtistReq
import com.poulastaa.data.model.setup.artist.SuggestArtistResponse

interface ArtistRepository {
    suspend fun suggestArtist(
        req: SuggestArtistReq,
        countryId: Int
    ): SuggestArtistResponse

    suspend fun storeArtist(
        helper: UserTypeHelper,
        artistNameList: List<String>
    ): StoreArtistResponse
}