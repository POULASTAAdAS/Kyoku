package com.poulastaa.main.domain.model

data class DtoSyncData<T>(
    val removeIdList: List<Long>,
    val newData: List<T>,
)
