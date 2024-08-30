package com.poulastaa.data.model

import kotlinx.serialization.Serializable

@Serializable
data class SyncDto<T>(
    val removeIdList: List<Long> = emptyList(),
    val newAlbumList: List<T> = emptyList(),
)