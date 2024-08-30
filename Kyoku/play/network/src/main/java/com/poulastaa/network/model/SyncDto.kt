package com.poulastaa.network.model

import kotlinx.serialization.Serializable

@Serializable
data class SyncDto<T>(
    val removeIdList: List<Long>,
    val newAlbumList: List<T>
)