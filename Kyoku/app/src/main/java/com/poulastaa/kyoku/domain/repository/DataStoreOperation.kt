package com.poulastaa.kyoku.domain.repository

import kotlinx.coroutines.flow.Flow

interface DataStoreOperation {
    suspend fun storeSignedInState(signedInState: String)
    fun readSignedInState(): Flow<String>

    suspend fun storeUsername(username: String)
    fun readUsername(): Flow<String>

    suspend fun storeProfilePic(uir: String)
    fun readProfilePic(): Flow<String>

    suspend fun storeEmail(email: String)
    fun readEmail(): Flow<String>

    suspend fun storePassword(password: String)
    fun readPassword(): Flow<String>

    suspend fun storeCookieOrAccessToken(data: String)
    fun readTokenOrCookie(): Flow<String>

    suspend fun storeRefreshToken(data: String)
    fun readRefreshToken(): Flow<String>

    suspend fun storeAuthType(data: String)
    fun readAuthType(): Flow<String>

    suspend fun storeBDate(date: String)
    fun readBDate(): Flow<String>

    suspend fun storeLibraryDataSortType(sortType: Boolean)
    fun readLibraryDataSortType(): Flow<Boolean>
}