package com.poulastaa.core.domain.repository

import com.poulastaa.core.domain.model.DtoUser
import com.poulastaa.core.domain.model.SavedScreen
import kotlinx.coroutines.flow.Flow

interface DatastoreRepository {
    suspend fun storeThem(them: Boolean)
    fun readThem(): Flow<Boolean>

    suspend fun storeSignInState(state: SavedScreen)
    suspend fun readSignInState(): SavedScreen

    suspend fun storeTokenOrCookie(data: String)
    fun readTokenOrCookie(): Flow<String>

    suspend fun storeRefreshToken(data: String)
    suspend fun readRefreshToken(): String

    suspend fun storeLocalUser(user: DtoUser)
    suspend fun readLocalUser(): DtoUser
    suspend fun updateBDate(bDate: String)

    suspend fun storeLibraryViewType(isGrid: Boolean)
    fun readLibraryViewType(): Flow<Boolean>

    suspend fun logOut()
}