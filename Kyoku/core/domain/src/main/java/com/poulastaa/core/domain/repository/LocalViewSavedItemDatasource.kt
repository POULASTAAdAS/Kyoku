package com.poulastaa.core.domain.repository

import com.poulastaa.core.domain.model.DtoAlbum
import com.poulastaa.core.domain.model.DtoArtist
import com.poulastaa.core.domain.model.DtoPlaylist

interface LocalViewSavedItemDatasource {
    suspend fun getSavedArtist(): List<DtoArtist>
    suspend fun getSavedAlbum(): Map<String?, DtoAlbum>
    suspend fun getSavedPlaylist(): Map<Pair<List<String>, Int>, DtoPlaylist>
}