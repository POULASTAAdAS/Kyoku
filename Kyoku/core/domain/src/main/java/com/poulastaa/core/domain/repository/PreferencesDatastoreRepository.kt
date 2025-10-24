package com.poulastaa.core.domain.repository

import com.poulastaa.core.domain.model.DtoUser
import com.poulastaa.core.domain.utils.JWTToken
import kotlinx.coroutines.flow.Flow

interface PreferencesDatastoreRepository {
    suspend fun saveUser(user: DtoUser)
    suspend fun getUser(): DtoUser?
    fun getUserFlow(): Flow<DtoUser?>
    suspend fun saveAccessToken(token: JWTToken)
    fun getAccessTokenFlow(): Flow<String?>
    suspend fun getAccessTokenFirst(): String?
    suspend fun saveRefreshToken(token: JWTToken)
}