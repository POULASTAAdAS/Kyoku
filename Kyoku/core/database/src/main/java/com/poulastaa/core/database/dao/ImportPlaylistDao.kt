package com.poulastaa.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.poulastaa.core.database.relation.PlaylistWithSong
import com.poulastaa.core.database.relation.SongIdWithArtistName
import com.poulastaa.core.domain.model.DtoSongInfo
import com.poulastaa.core.domain.model.SongId
import kotlinx.coroutines.flow.Flow

@Dao
interface ImportPlaylistDao {
    @Transaction
    @Query("select * from EntityPlaylist")
    fun getAllPlaylist(): Flow<List<PlaylistWithSong>>

    @Query("select * from EntitySongInfo where songId in (:idList)")
    suspend fun getSongInfo(idList: List<SongId>): List<DtoSongInfo>

    @Query(
        """
        SELECT EntitySong.id as songId,GROUP_CONCAT(EntityArtist.name) AS artist
        FROM EntityArtist
        JOIN EntityRelationSongArtist ON EntityRelationSongArtist.artistId = EntityArtist.id
        JOIN EntitySong ON EntityRelationSongArtist.songId = EntitySong.id
        WHERE EntitySong.id IN (:idList)
        GROUP BY EntitySong.id
    """
    )
    suspend fun getArtistNameOnSongId(idList: List<SongId>): List<SongIdWithArtistName>
}