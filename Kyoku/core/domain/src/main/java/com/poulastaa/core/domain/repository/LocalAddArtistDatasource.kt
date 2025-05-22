package com.poulastaa.core.domain.repository

import com.poulastaa.core.domain.model.ArtistId
import com.poulastaa.core.domain.model.DtoArtist

interface LocalAddArtistDatasource {
    suspend fun loadSavedArtist(): List<ArtistId>
    suspend fun savedArtist(list: List<DtoArtist>)
}