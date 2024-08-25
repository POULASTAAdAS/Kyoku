package com.poulastaa.core.database.repository

import com.poulastaa.core.database.KyokuDatabase
import com.poulastaa.core.database.dao.SettingDao
import com.poulastaa.core.domain.repository.setting.LocalSettingDatasource
import javax.inject.Inject

class RoomLocalSettingDatasource @Inject constructor(
    private val database: KyokuDatabase,
    private val settingDao: SettingDao,
) : LocalSettingDatasource {
    override suspend fun logOut(): Boolean {
        database.clearAllTables()
        return true
    }
}