package com.poulastaa.kyoku.domain.repository

import kotlinx.coroutines.flow.Flow

interface DataStoreOperation {
    suspend fun storeSignedInState(signedInState: Boolean)
    fun readSignedInState(): Flow<Boolean>
}