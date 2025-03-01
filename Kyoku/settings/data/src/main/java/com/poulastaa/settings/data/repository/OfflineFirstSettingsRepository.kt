package com.poulastaa.settings.data.repository

import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.EmptyResult
import com.poulastaa.core.domain.Result
import com.poulastaa.core.domain.asEmptyDataResult
import com.poulastaa.core.domain.model.DtoUser
import com.poulastaa.core.domain.repository.DatastoreRepository
import com.poulastaa.core.domain.repository.LocalSettingsDatasource
import com.poulastaa.settings.domain.repository.RemoteSettingDatasource
import com.poulastaa.settings.domain.repository.SettingRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import javax.inject.Inject

internal class OfflineFirstSettingsRepository @Inject constructor(
    private val ds: DatastoreRepository,
    private val remote: RemoteSettingDatasource,
    private val local: LocalSettingsDatasource,
    private val scope: CoroutineScope,
) : SettingRepository {
    override suspend fun getUser(): DtoUser = ds.readLocalUser()

    override suspend fun logOut() = delete()

    override suspend fun deleteAccount(): EmptyResult<DataError.Network> {
        val result = remote.deleteAccount()
        if (result is Result.Success && result.data) delete()

        return result.asEmptyDataResult()
    }

    private suspend fun delete() = listOf(
        scope.async { local.clearDatabase() },
        scope.async { ds.logOut() }
    ).awaitAll().let {}
}