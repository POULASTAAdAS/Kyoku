package com.poulastaa.core.domain.repository

import com.poulastaa.core.domain.model.SavedScreen
import com.poulastaa.core.domain.model.UserDto
import kotlinx.coroutines.flow.Flow

interface DatastoreRepository {
    suspend fun storeSignInState(state: SavedScreen)
    suspend fun readSignInState(): SavedScreen

    suspend fun storeTokenOrCookie(data: String)
    fun readTokenOrCookie(): Flow<String>

    suspend fun storeRefreshToken(data: String)
    suspend fun readRefreshToken(): String

    suspend fun storeLocalUser(user: UserDto)
    suspend fun readLocalUser(): UserDto

    suspend fun storeLibraryViewType(isGrid: Boolean)
    suspend fun readLibraryViewType(): Boolean

    suspend fun logOut()
}