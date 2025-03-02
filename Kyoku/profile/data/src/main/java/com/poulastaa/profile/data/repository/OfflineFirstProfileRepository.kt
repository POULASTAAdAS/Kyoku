package com.poulastaa.profile.data.repository

import com.poulastaa.core.domain.model.DtoUser
import com.poulastaa.core.domain.repository.DatastoreRepository
import com.poulastaa.core.domain.repository.LocalProfileDatasource
import com.poulastaa.profile.domain.model.DtoItemType
import com.poulastaa.profile.domain.model.SavedItem
import com.poulastaa.profile.domain.repository.ProfileRepository
import javax.inject.Inject

internal class OfflineFirstProfileRepository @Inject constructor(
    private val ds: DatastoreRepository,
    private val local: LocalProfileDatasource,
) : ProfileRepository {
    override suspend fun getUser(): DtoUser = ds.readLocalUser()

    override suspend fun getBData(): String = ds.readBDate() ?: ""

    override suspend fun getSavedData(): List<SavedItem> = local.getSavedData().map {
        when (it.first) {  // i am to lazy to create another enum class deal with it :)
            1 -> SavedItem(it.second, DtoItemType.PLAYLIST)
            2 -> SavedItem(it.second, DtoItemType.ALBUM)
            3 -> SavedItem(it.second, DtoItemType.ARTIST)
            else -> SavedItem(it.second, DtoItemType.FAVOURITE)
        }
    }
}