package com.poulastaa.core.domain.repository

import com.poulastaa.core.domain.model.DtoPlaylist

interface LocalCreatePlaylistDatasource {
    suspend fun savePlaylist(playlist: DtoPlaylist)
}