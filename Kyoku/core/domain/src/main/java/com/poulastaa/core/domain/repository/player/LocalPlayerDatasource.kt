package com.poulastaa.core.domain.repository.player

import com.poulastaa.core.domain.PlayType
import com.poulastaa.core.domain.PlayerInfo
import com.poulastaa.core.domain.model.PlayerSong
import kotlinx.coroutines.flow.Flow

typealias TypeName = String

interface LocalPlayerDatasource {
    suspend fun loadData(id: Long, type: PlayType)
    fun getInfo(): Flow<PlayerInfo>
    fun getSongs(): Flow<List<PlayerSong>>
}