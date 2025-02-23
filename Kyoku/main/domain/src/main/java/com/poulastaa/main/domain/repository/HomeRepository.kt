package com.poulastaa.main.domain.repository

import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.EmptyResult
import com.poulastaa.main.domain.model.PayloadHomeData

interface HomeRepository {
    suspend fun getHome(): EmptyResult<DataError.Network>
    suspend fun loadData(): PayloadHomeData
}