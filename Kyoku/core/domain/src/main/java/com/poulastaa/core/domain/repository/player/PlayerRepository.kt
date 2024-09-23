package com.poulastaa.core.domain.repository.player

import com.poulastaa.core.domain.PlayType
import com.poulastaa.core.domain.PlayerInfo
import com.poulastaa.core.domain.model.ArtistWithPopularity
import com.poulastaa.core.domain.model.PlayerSong
import com.poulastaa.core.domain.model.SongOtherData
import com.poulastaa.core.domain.utils.DataError
import com.poulastaa.core.domain.utils.EmptyResult
import com.poulastaa.core.domain.utils.Result
import kotlinx.coroutines.flow.Flow

interface PlayerRepository {
    suspend fun loadData(
        id: Long,
        type: PlayType,
        isShuffled: Boolean = false,
    ): EmptyResult<DataError.Network>

    fun getInfo(): Flow<PlayerInfo>
    fun getSongs(): Flow<List<PlayerSong>>

    suspend fun getArtistOnSongId(songId: Long): Result<List<ArtistWithPopularity>, DataError.Network>
    suspend fun getOtherInfo(songId: Long): Result<SongOtherData, DataError.Network>
    fun close()
}