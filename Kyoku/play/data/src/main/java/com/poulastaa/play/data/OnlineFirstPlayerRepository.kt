package com.poulastaa.play.data

import android.content.Context
import com.poulastaa.core.domain.PlayType
import com.poulastaa.core.domain.PlayerInfo
import com.poulastaa.core.domain.model.PlayerSong
import com.poulastaa.core.domain.repository.player.LocalPlayerDatasource
import com.poulastaa.core.domain.repository.player.PlayerRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OnlineFirstPlayerRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val local: LocalPlayerDatasource
) : PlayerRepository {
    override suspend fun loadData(id: Long, type: PlayType) = local.loadData(id, type)

    override fun getInfo(): Flow<PlayerInfo> = local.getInfo()

    override fun getSongs(): Flow<List<PlayerSong>> = local.getSongs()
}