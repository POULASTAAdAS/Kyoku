package com.poulastaa.profile.data.repository

import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.EmptyResult
import com.poulastaa.core.domain.Result
import com.poulastaa.core.domain.model.DtoUser
import com.poulastaa.core.domain.repository.DatastoreRepository
import com.poulastaa.core.domain.repository.LocalProfileDatasource
import com.poulastaa.profile.domain.model.DtoItemType
import com.poulastaa.profile.domain.model.SavedItem
import com.poulastaa.profile.domain.repository.ProfileRepository
import com.poulastaa.profile.domain.repository.RemoteProfileDatasource
import javax.inject.Inject

internal class OfflineFirstProfileRepository @Inject constructor(
    private val ds: DatastoreRepository,
    private val remote: RemoteProfileDatasource,
    private val local: LocalProfileDatasource,
) : ProfileRepository {
    override suspend fun getUser(): DtoUser = ds.readLocalUser()

    override suspend fun getBData(): String {
        val localBDate = ds.readBDate()

        if (localBDate == null) {
            val result = remote.getBDate()
            if (result is Result.Success) {
                ds.updateBDate(result.data)
                return result.data
            }
        }

        return localBDate ?: ""
    }

    override suspend fun getSavedData(): List<SavedItem> = local.getSavedData().map {
        when (it.first) {  // i am not creating another enum class deal with it :)
            1 -> SavedItem(it.second, DtoItemType.PLAYLIST)
            2 -> SavedItem(it.second, DtoItemType.ALBUM)
            3 -> SavedItem(it.second, DtoItemType.ARTIST)
            else -> SavedItem(it.second, DtoItemType.FAVOURITE)
        }
    }

    override suspend fun updateUsername(
        name: String,
    ): EmptyResult<DataError.Network> = remote.updateUsername(name)
}