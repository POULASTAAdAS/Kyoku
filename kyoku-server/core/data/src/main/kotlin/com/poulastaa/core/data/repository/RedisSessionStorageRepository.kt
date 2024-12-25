package com.poulastaa.core.data.repository

import com.poulastaa.core.domain.repository.LocalSessionCacheDatasource
import io.ktor.server.sessions.*

class RedisSessionStorageRepository(
    private val storage: LocalSessionCacheDatasource,
) : SessionStorage {
    override suspend fun write(id: String, value: String) = storage.write(id, value)

    override suspend fun invalidate(id: String) = storage.invalidate(id)

    override suspend fun read(id: String): String =
        storage.read(id) ?: throw NoSuchElementException("Session $id not found")
}