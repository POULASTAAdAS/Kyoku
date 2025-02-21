package com.poulastaa.main.domain.repository

import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.EmptyResult

interface HomeRepository {
    suspend fun getHome(): EmptyResult<DataError.Network>
}