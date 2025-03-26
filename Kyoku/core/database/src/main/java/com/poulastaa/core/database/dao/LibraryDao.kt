package com.poulastaa.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.poulastaa.core.database.entity.EntityAlbum
import com.poulastaa.core.database.entity.EntityArtist
import com.poulastaa.core.database.model.PayloadPrevPlaylist
import kotlinx.coroutines.flow.Flow

@Dao
internal interface LibraryDao {
    @Query(
        """
        Select EntityPlaylist.id , EntityPlaylist.name as title , EntitySong.poster from EntityPlaylist
        left join EntityRelationSongPlaylist on EntityPlaylist.id = EntityRelationSongPlaylist.playlistId
        left join EntitySong on EntityRelationSongPlaylist.songId = EntitySong.id
    """
    )
    fun loadSavedPrevPlaylist(): Flow<List<PayloadPrevPlaylist>>

    @Query("Select * from EntityArtist where id in (Select * from EntitySavedArtist)")
    fun loadSavedPrevArtist(): Flow<List<EntityArtist>>

    @Query("Select * from EntityAlbum where EntityAlbum.id in (Select * from EntitySavedAlbum)")
    fun loadSavedPrevAlbum(): Flow<List<EntityAlbum>>
}