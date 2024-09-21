package com.poulastaa.core.domain.model

data class SyncData<T>(
    val removeIdList: List<Long>,
    val newAlbumList: List<T>,
)