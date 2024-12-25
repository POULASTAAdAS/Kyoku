package com.poulastaa.core.domain.repository

interface LocalSessionCacheDatasource {
    suspend fun write(id: String, value: String)
    suspend fun invalidate(id: String)
    suspend fun read(id: String): String?
}