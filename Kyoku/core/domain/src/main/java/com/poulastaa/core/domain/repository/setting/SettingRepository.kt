package com.poulastaa.core.domain.repository.setting

interface SettingRepository {
    suspend fun logOut(): Boolean
}