package com.poulastaa.main.domain.repository

import com.poulastaa.main.domain.model.PayloadSavedItem
import kotlinx.coroutines.flow.Flow

interface LibraryRepository {
    suspend fun loadSavedPlaylist(): Flow<List<PayloadSavedItem>>
    fun loadSavedArtist(): Flow<List<PayloadSavedItem>>
    fun loadSavedAlbum(): Flow<List<PayloadSavedItem>>
}