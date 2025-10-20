package com.poulastaa.core.domain.repository

import com.poulastaa.core.domain.model.DtoUser
import kotlinx.coroutines.flow.Flow

interface PreferencesDatastoreRepository {
    suspend fun saveUser(user: DtoUser)
    suspend fun getUser(): DtoUser?
    suspend fun getUserFlow(): Flow<DtoUser?>
}