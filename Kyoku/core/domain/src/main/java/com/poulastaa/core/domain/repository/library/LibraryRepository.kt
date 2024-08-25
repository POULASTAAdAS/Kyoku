package com.poulastaa.core.domain.repository.library

import com.poulastaa.core.domain.LibraryDataType
import com.poulastaa.core.domain.model.PinnedData
import com.poulastaa.core.domain.model.PinnedType
import com.poulastaa.core.domain.utils.DataError
import com.poulastaa.core.domain.utils.EmptyResult
import com.poulastaa.core.domain.utils.SavedAlbum
import com.poulastaa.core.domain.utils.SavedArtist
import com.poulastaa.core.domain.utils.SavedPlaylist
import kotlinx.coroutines.flow.Flow

interface LibraryRepository {
    fun getPlaylist(): Flow<SavedPlaylist>
    fun getAlbum(): Flow<SavedAlbum>
    fun getArtist(): Flow<SavedArtist>
    suspend fun isFavourite(): Boolean

    suspend fun getPinnedData(): Flow<List<PinnedData>>

    suspend fun checkIfPinned(id: Long, type: PinnedType): Boolean

    suspend fun pinData(id: Long, type: LibraryDataType): EmptyResult<DataError.Network>
    suspend fun unPinData(id: Long, type: LibraryDataType): EmptyResult<DataError.Network>

    suspend fun deleteSavedData(id: Long, type: LibraryDataType): EmptyResult<DataError.Network>
}