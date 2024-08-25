package com.poulastaa.core.domain.repository.setting

interface LocalSettingDatasource {
    suspend fun logOut(): Boolean
}