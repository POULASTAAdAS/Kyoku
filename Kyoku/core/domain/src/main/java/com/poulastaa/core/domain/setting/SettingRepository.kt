package com.poulastaa.core.domain.setting

interface SettingRepository {
    suspend fun logOut(): Boolean
}