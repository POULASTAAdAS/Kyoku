package com.poulastaa.network.model

import com.poulastaa.core.domain.LibraryDataType
import kotlinx.serialization.Serializable

@Serializable
data class PinReq(
    val id: Long,
    val type: LibraryDataType,
)
