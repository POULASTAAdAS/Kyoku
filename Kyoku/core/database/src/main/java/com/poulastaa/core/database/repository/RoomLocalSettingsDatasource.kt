package com.poulastaa.core.database.repository

import com.poulastaa.core.database.KyokuDatabase
import com.poulastaa.core.domain.repository.LocalSettingsDatasource
import javax.inject.Inject

internal class RoomLocalSettingsDatasource @Inject constructor(
    private val database: KyokuDatabase,
) : LocalSettingsDatasource {
    override suspend fun clearDatabase() = database.clearAllTables()
}