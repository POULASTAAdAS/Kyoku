package com.poulastaa.core.domain.home

import com.poulastaa.core.domain.model.DayType
import com.poulastaa.core.domain.model.HomeData
import com.poulastaa.core.domain.model.NewHome

interface LocalHomeDatasource {
    suspend fun storeNewHomeResponse(
        dayType: DayType,
        response: NewHome,
    )

    suspend fun isNewUser(): Boolean

    suspend fun loadHomeStaticData(): HomeData
}