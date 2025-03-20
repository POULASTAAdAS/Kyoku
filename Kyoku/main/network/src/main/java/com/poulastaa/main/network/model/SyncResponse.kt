package com.poulastaa.main.network.model

data class SyncResponse<T>(
    val removeIdList: List<Long>,
    val newData: List<T>,
)
