package com.poulastaa.core.domain.repository.player

interface PlayerRepository {
    suspend fun loadBackgroundColor(url: String)
}