package com.poulastaa.core.database.repository

import com.poulastaa.core.domain.model.SavedScreen
import com.poulastaa.core.domain.repository.DatastoreRepository
import com.poulastaa.core.domain.repository.LocalBDateDatasource
import javax.inject.Inject

class DatastoreLocalBDateDatasource @Inject constructor(
    private val ds: DatastoreRepository,
) : LocalBDateDatasource {
    override suspend fun setBDate(bDate: String) {
        ds.updateBDate(bDate)
        ds.storeSignInState(SavedScreen.PIC_GENRE)
    }
}