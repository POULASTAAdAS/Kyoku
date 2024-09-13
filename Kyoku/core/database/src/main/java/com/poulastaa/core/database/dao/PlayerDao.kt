package com.poulastaa.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.poulastaa.core.database.entity.PlayerInfoEntity
import com.poulastaa.core.database.entity.PlayerSongEntity
import com.poulastaa.core.database.entity.PlaylistEntity
import com.poulastaa.core.database.entity.SongEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlayerDao {
    @Query("select * from PlaylistEntity where id = :id")
    suspend fun getPlaylist(id: Long): PlaylistEntity

    @Query(
        """
        select SongEntity.* from SongEntity
        join SongPlaylistRelationEntity on SongEntity.id = SongPlaylistRelationEntity.songId
        where SongPlaylistRelationEntity.playlistId = :id
    """
    )
    suspend fun getPlaylistSongs(id: Long): List<SongEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun loadPlayerSongs(entrys: List<PlayerSongEntity>)

    @Query("delete from PlayerSongEntity")
    suspend fun clearPlayerSongs()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun loadInfo(entry: PlayerInfoEntity)

    @Query("delete from PlayerInfoEntity")
    suspend fun clearLoadInfo()

    @Query("select * from PlayerInfoEntity limit 1")
    fun getInfo(): Flow<PlayerInfoEntity>

    @Query("select * from PlayerSongEntity")
    fun getSong(): Flow<List<PlayerSongEntity>>
}