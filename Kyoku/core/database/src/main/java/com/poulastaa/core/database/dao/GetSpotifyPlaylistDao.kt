package com.poulastaa.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.poulastaa.core.database.model.PlaylistResult
import kotlinx.coroutines.flow.Flow

@Dao
interface GetSpotifyPlaylistDao {
    @Query(
        """
        select SonEntity.id as songId , SonEntity.coverImage as songCoverImage,
        SonEntity.title as songTitle, PlaylistEntity.id as playlistId,
        PlaylistEntity.name as playlistName, ArtistEntity.id as artistId, 
        ArtistEntity.name as artistName, ArtistEntity.coverImage as artistCoverImage from SonEntity
        join PlaylistEntity on PlaylistEntity.id = SongPlaylistRelationEntity.playlistId
        join SongArtistRelationEntity on SongArtistRelationEntity.songId = SonEntity.id
        join ArtistEntity on ArtistEntity.id = SongArtistRelationEntity.artistId
        join SongPlaylistRelationEntity on SongPlaylistRelationEntity.songId = SonEntity.id
        where PlaylistEntity.id = :playlistId
    """
    )
    fun getPlaylist(playlistId: Long): Flow<List<PlaylistResult>>
}