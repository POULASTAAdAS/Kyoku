package com.poulastaa.core.domain.repository

import com.poulastaa.core.domain.model.DtoAlbum
import com.poulastaa.core.domain.model.DtoArtist
import com.poulastaa.core.domain.model.DtoPlaylist
import com.poulastaa.core.domain.model.DtoPlaylistPayload

interface LocalViewSavedItemDatasource {
    suspend fun getSavedArtist(): List<DtoArtist>
    suspend fun getSavedAlbum(): Map<String?, DtoAlbum>
    suspend fun getSavedPlaylist(): List<DtoPlaylistPayload>
}