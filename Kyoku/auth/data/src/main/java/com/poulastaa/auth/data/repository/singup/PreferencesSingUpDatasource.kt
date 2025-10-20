package com.poulastaa.auth.data.repository.singup

import com.poulastaa.auth.domain.email_signup.EmailSingUpLocalDatasource
import com.poulastaa.core.domain.model.DtoUser
import com.poulastaa.core.domain.repository.PreferencesDatastoreRepository
import com.poulastaa.core.domain.utils.JWTToken
import jakarta.inject.Inject

internal class PreferencesSingUpDatasource @Inject constructor(
    private val ds: PreferencesDatastoreRepository,
) : EmailSingUpLocalDatasource {
    override suspend fun saveUser(user: DtoUser) = ds.saveUser(user)
    override suspend fun saveAccessToken(token: JWTToken) = ds.saveAccessToken(token)
    override suspend fun saveRefreshToken(token: JWTToken) = ds.saveRefreshToken(token)
}