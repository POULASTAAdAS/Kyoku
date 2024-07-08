package com.poulastaa.core.domain.home

import com.poulastaa.core.domain.model.NewHome

interface HomeRepository {
    suspend fun storeNewHomeResponse(response: NewHome): Boolean
}