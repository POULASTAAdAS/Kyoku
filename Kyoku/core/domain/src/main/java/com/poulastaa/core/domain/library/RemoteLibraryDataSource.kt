package com.poulastaa.core.domain.library

import com.poulastaa.core.domain.PinReqType
import com.poulastaa.core.domain.model.PinnedType
import com.poulastaa.core.domain.utils.DataError
import com.poulastaa.core.domain.utils.EmptyResult

interface RemoteLibraryDataSource {
    suspend fun pinData(id: Long, pinnedType: PinReqType): EmptyResult<DataError.Network>
    suspend fun unPinData(id: Long, pinnedType: PinReqType): EmptyResult<DataError.Network>
}