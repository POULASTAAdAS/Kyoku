package com.poulastaa.play.data

import com.poulastaa.core.domain.DataStoreRepository
import com.poulastaa.core.domain.setting.LocalSettingDatasource
import com.poulastaa.core.domain.setting.SettingRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import javax.inject.Inject

class OfflineFirstSettingRepository @Inject constructor(
    private val ds: DataStoreRepository,
    private val local: LocalSettingDatasource,
    private val application: CoroutineScope,
) : SettingRepository {
    override suspend fun logOut(): Boolean = application.async {
        val ds = async { ds.logOut() }
        val db = async { local.logOut() }

        ds.await()
        db.await()
    }.await()
}