package com.poulastaa.core.domain.model

data class DtoSyncPayload<T>(
    val removeIdList: List<Long>,
    val newData: List<T>,
)
