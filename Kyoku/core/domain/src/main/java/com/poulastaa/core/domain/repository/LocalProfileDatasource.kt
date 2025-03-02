package com.poulastaa.core.domain.repository

interface LocalProfileDatasource {
    suspend fun getSavedData(): List<Pair<Int, Int>>
}