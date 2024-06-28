package com.poulastaa.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.poulastaa.core.database.entity.ArtistEntity
import com.poulastaa.core.database.entity.PlaylistEntity
import com.poulastaa.core.database.entity.SongEntity
import com.poulastaa.core.database.entity.relation.SongArtistRelationEntity
import com.poulastaa.core.database.entity.relation.SongPlaylistRelationEntity

@Dao
interface CommonDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSong(song: SongEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSongs(songs: List<SongEntity>)


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertArtist(artist: ArtistEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertArtists(artists: List<ArtistEntity>)


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPlaylist(playlist: PlaylistEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPlaylists(playlists: List<PlaylistEntity>)


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSongArtistRelation(songArtistRelations: SongArtistRelationEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSongArtistRelations(list: List<SongArtistRelationEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSongPlaylistRelation(list: List<SongPlaylistRelationEntity>)


    @Query("select id from SongEntity where coverImage = :url")
    suspend fun getSongOnUrl(url: String): Long?
}