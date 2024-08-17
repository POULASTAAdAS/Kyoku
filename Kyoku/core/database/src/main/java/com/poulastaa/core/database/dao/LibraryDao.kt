package com.poulastaa.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.poulastaa.core.database.entity.AlbumEntity
import com.poulastaa.core.database.entity.ArtistEntity
import com.poulastaa.core.database.entity.FavouriteEntity
import com.poulastaa.core.database.entity.PinnedEntity
import com.poulastaa.core.database.entity.PlaylistEntity
import com.poulastaa.core.domain.model.PinnedResult
import com.poulastaa.core.domain.model.PinnedType
import kotlinx.coroutines.flow.Flow

@Dao
interface LibraryDao {
    @Query("select * from ArtistEntity")
    fun getAllSavedArtist(): Flow<List<ArtistEntity>>

    @Query("select * from PinnedEntity")
    fun getPinnedData(): Flow<List<PinnedEntity>>

    @Query("select id from PinnedEntity where id = :id and type = :pinnedType")
    suspend fun checkIfPinned(id: Long, pinnedType: PinnedType): Long?

    @Query(
        """
        select PlaylistEntity.id , PlaylistEntity.name , SongEntity.coverImage from PlaylistEntity
        join SongPlaylistRelationEntity on SongPlaylistRelationEntity.playlistId = PlaylistEntity.id
        join SongEntity on SongEntity.id = SongPlaylistRelationEntity.songId
        where PlaylistEntity.id = :id limit 4
    """
    )
    suspend fun getPinnedPlaylist(id: Long): List<PinnedResult>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun pinData(entry: PinnedEntity)

    @Delete
    suspend fun unPinData(entry: PinnedEntity)

    @Query("select * from PlaylistEntity where id = :id")
    suspend fun getPlaylistOnId(id: Long): PlaylistEntity?

    @Delete
    suspend fun deletePlaylist(entry: PlaylistEntity)


    @Query("select * from ArtistEntity where id = :id")
    suspend fun getArtistOnId(id: Long): ArtistEntity?

    @Delete
    suspend fun deleteArtist(entry: ArtistEntity)

    @Query("select * from ALBUMENTITY where id = :id")
    suspend fun getAlbumOnId(id: Long): AlbumEntity?
    @Delete
    suspend fun deleteAlbum(entry: AlbumEntity)
}