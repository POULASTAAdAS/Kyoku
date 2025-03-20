package com.poulastaa.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.poulastaa.core.database.entity.EntityAlbum
import com.poulastaa.core.database.entity.EntityArtist
import com.poulastaa.core.database.entity.EntityPrevAlbum
import com.poulastaa.core.database.entity.EntityPrevArtist
import com.poulastaa.core.database.model.PayloadPrevPlaylist
import com.poulastaa.core.database.relation.SuggestedArtistWithSong
import com.poulastaa.core.domain.model.DtoPrevSong
import com.poulastaa.core.domain.model.SongId

@Dao
internal interface HomeDao {
    @Query("Select * from EntityAlbum where EntityAlbum.id in (Select * from EntitySavedAlbum) order by RANDOM() limit :limit")
    suspend fun getRandomSavedPrevAlbum(limit: Int): List<EntityAlbum>

    @Query("Select * from EntityArtist  where id in (Select * from EntitySavedArtist) order by RANDOM() limit :limit")
    suspend fun getRandomSavedPrevArtist(limit: Int): List<EntityArtist>

    @Query(
        """
        Select EntityPlaylist.id , EntityPlaylist.name as title , EntitySong.poster from EntityPlaylist
        join EntityRelationSongPlaylist on EntityPlaylist.id = EntityRelationSongPlaylist.playlistId
        join EntitySong on EntityRelationSongPlaylist.songId = EntitySong.id
        where EntityPlaylist.id in (Select playlistId from EntityPlaylist order by RANDOM() limit :limit)
    """
    )
    suspend fun getRandomSavedPrevPlaylist(limit: Int): List<PayloadPrevPlaylist>

    @Query("Select EntityPrevExplore.dataId from EntityPrevExplore where typeId = :type order by RANDOM()")
    suspend fun getPrevExploreType(type: Int): List<SongId>

    @Query("Select * from EntityPrevSong where id in (:list)")
    suspend fun getPrevSong(list: List<SongId>): List<DtoPrevSong>

    @Query("Select * from EntityPrevArtist")
    suspend fun getPrevArtist(): List<EntityPrevArtist>

    @Query("Select * from EntityPrevAlbum")
    suspend fun getPrevAlbum(): List<EntityPrevAlbum>

    @Transaction
    @Query("Select * from EntityPrevArtist")
    suspend fun getSuggestedArtistSong(): List<SuggestedArtistWithSong>
}