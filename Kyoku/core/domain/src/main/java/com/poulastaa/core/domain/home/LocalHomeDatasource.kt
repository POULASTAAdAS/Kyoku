package com.poulastaa.core.domain.home

import com.poulastaa.core.domain.model.NewHome

interface LocalHomeDatasource {
    suspend fun storeNewHomeResponse(response: NewHome): Boolean
}