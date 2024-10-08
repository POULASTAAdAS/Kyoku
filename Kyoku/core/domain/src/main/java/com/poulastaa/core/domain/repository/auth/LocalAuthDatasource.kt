package com.poulastaa.core.domain.repository.auth

import com.poulastaa.core.domain.model.DayType
import com.poulastaa.core.domain.model.LogInData

interface LocalAuthDatasource {
    suspend fun storeData(
        dayType: DayType = DayType.NIGHT,
        data: LogInData,
    )
}