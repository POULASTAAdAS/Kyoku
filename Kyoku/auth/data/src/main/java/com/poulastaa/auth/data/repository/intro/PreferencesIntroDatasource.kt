package com.poulastaa.auth.data.repository.intro

import com.poulastaa.auth.domain.intro.IntroLocalDatasource
import com.poulastaa.core.domain.model.DtoUser
import com.poulastaa.core.domain.repository.PreferencesDatastoreRepository
import jakarta.inject.Inject

internal class PreferencesIntroDatasource @Inject constructor(
    private val ds: PreferencesDatastoreRepository,
) : IntroLocalDatasource {
    override suspend fun saveUser(user: DtoUser) = ds.saveUser(user)
}