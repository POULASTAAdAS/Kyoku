package com.poulastaa.core.domain.repository

interface LocalSettingsDatasource {
    suspend fun clearDatabase()
}