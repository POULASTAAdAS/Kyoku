package com.poulastaa.core.domain

import kotlinx.coroutines.flow.Flow

interface DataStoreRepository {
    suspend fun storeSignInState(state: ScreenEnum)
    suspend fun readSignInState(): ScreenEnum

    suspend fun storeTokenOrCookie(data: String)
    fun readTokenOrCookie(): Flow<String>
}