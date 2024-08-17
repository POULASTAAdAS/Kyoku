package com.poulastaa.core.domain.library

import com.poulastaa.core.domain.LibraryDataType
import com.poulastaa.core.domain.utils.DataError
import com.poulastaa.core.domain.utils.EmptyResult

interface RemoteLibraryDataSource {
    suspend fun pinData(id: Long, type: LibraryDataType): EmptyResult<DataError.Network>
    suspend fun unPinData(id: Long, type: LibraryDataType): EmptyResult<DataError.Network>

    suspend fun deleteSavedData(id: Long, type: LibraryDataType): EmptyResult<DataError.Network>
}