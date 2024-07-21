package com.poulastaa.core.domain.library

import com.poulastaa.core.domain.utils.SavedArtist
import com.poulastaa.core.domain.utils.SavedPlaylist
import kotlinx.coroutines.flow.Flow

interface LibraryRepository {
    fun getPlaylist(): Flow<SavedPlaylist>
    fun getArtist(): Flow<SavedArtist>
}