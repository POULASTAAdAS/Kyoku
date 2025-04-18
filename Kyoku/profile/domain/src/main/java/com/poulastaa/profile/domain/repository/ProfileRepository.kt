package com.poulastaa.profile.domain.repository

import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.EmptyResult
import com.poulastaa.core.domain.model.DtoUser
import com.poulastaa.profile.domain.model.SavedItem

interface ProfileRepository {
    suspend fun getUser(): DtoUser
    suspend fun getBData(): String
    suspend fun getSavedData(): List<SavedItem>
    suspend fun updateUsername(name: String): EmptyResult<DataError.Network>
}