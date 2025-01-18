package com.poulastaa.core.domain.repository

interface LocalBDateDatasource {
    suspend fun setBDate(bDate: String)
}