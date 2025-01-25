package com.poulastaa.core.domain.repository

import com.poulastaa.core.domain.model.DtoArtist

interface LocalSetArtistDatasource {
    suspend fun storeArtist(list: List<DtoArtist>)
}