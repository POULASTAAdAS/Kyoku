package com.poulastaa.core.domain.setting

interface LocalSettingDatasource {
    suspend fun logOut(): Boolean
}