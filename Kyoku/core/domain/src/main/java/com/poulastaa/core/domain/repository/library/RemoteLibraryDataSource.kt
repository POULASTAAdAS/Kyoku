package com.poulastaa.core.domain.repository.library

import com.poulastaa.core.domain.LibraryDataType
import com.poulastaa.core.domain.model.Playlist
import com.poulastaa.core.domain.utils.DataError
import com.poulastaa.core.domain.utils.EmptyResult
import com.poulastaa.core.domain.utils.Result

interface RemoteLibraryDataSource {
    suspend fun pinData(id: Long, type: LibraryDataType): EmptyResult<DataError.Network>
    suspend fun unPinData(id: Long, type: LibraryDataType): EmptyResult<DataError.Network>

    suspend fun deleteSavedData(id: Long, type: LibraryDataType): EmptyResult<DataError.Network>

    suspend fun createPlaylist(name: String): Result<Playlist, DataError.Network>
}