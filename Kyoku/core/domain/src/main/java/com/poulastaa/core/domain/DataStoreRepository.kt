package com.poulastaa.core.domain

import kotlinx.coroutines.flow.Flow

interface DataStoreRepository {
    suspend fun storeSignInState(state: StartScreen)
    suspend fun readSignInState(): StartScreen

    suspend fun storeTokenOrCookie(data: String)
    fun readTokenOrCookie(): Flow<String>
}