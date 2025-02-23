package com.poulastaa.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.poulastaa.core.database.entity.EntityAlbum
import com.poulastaa.core.database.entity.EntityArtist
import com.poulastaa.core.database.entity.EntityCountry
import com.poulastaa.core.database.entity.EntityExplore
import com.poulastaa.core.database.entity.EntityFavourite
import com.poulastaa.core.database.entity.EntityGenre
import com.poulastaa.core.database.entity.EntityPlaylist
import com.poulastaa.core.database.entity.EntityPrevAlbum
import com.poulastaa.core.database.entity.EntityPrevArtist
import com.poulastaa.core.database.entity.EntityPrevExplore
import com.poulastaa.core.database.entity.EntityPrevSong
import com.poulastaa.core.database.entity.EntityRelationArtistAlbum
import com.poulastaa.core.database.entity.EntityRelationArtistCountry
import com.poulastaa.core.database.entity.EntityRelationArtistGenre
import com.poulastaa.core.database.entity.EntityRelationSongAlbum
import com.poulastaa.core.database.entity.EntityRelationSongArtist
import com.poulastaa.core.database.entity.EntityRelationSongCountry
import com.poulastaa.core.database.entity.EntityRelationSongPlaylist
import com.poulastaa.core.database.entity.EntityRelationSuggestedSongByArtist
import com.poulastaa.core.database.entity.EntitySong
import com.poulastaa.core.database.entity.EntitySongInfo
import com.poulastaa.core.domain.model.AlbumId
import com.poulastaa.core.domain.model.ArtistId
import com.poulastaa.core.domain.model.PlaylistId
import com.poulastaa.core.domain.model.SongId

@Dao
internal interface RootDao {
    // Song
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSong(song: EntitySong): SongId

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSong(list: List<EntitySong>): List<SongId>


    // Artist
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertArtist(artist: EntityArtist): ArtistId

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertArtist(list: List<EntityArtist>): List<ArtistId>


    // Album
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAlbum(album: EntityAlbum): AlbumId

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAlbum(list: List<EntityAlbum>): List<AlbumId>


    // Country
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCountry(country: EntityCountry): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCountry(list: List<EntityCountry>): List<Long>


    // Genre
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertGenre(genre: EntityGenre): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertGenre(list: List<EntityGenre>): List<Long>


    // Playlist
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPlaylist(playlist: EntityPlaylist): PlaylistId

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPlaylist(list: List<EntityPlaylist>): List<PlaylistId>

    // Favourite
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFavourite(favourite: EntityFavourite)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFavourite(list: List<EntityFavourite>)

    // PREVIEW
    // ---------------------------------------------------------------------------------------------

    // Song
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPrevSong(song: EntityPrevSong): SongId

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPrevSong(list: List<EntityPrevSong>): List<SongId>


    // Artist
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPrevArtist(artist: EntityPrevArtist): ArtistId

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPrevArtist(list: List<EntityPrevArtist>): List<ArtistId>


    // Album
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPrevAlbum(album: EntityPrevAlbum): AlbumId

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPrevAlbum(list: List<EntityPrevAlbum>): List<AlbumId>


    // SongInfo
    // ---------------------------------------------------------------------------------------------

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSongInfo(songInfo: EntitySongInfo)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSongInfo(list: List<EntitySongInfo>)

    // Relation with song
    // ---------------------------------------------------------------------------------------------

    // Relation Song Album
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertRelationSongAlbum(relationSongAlbum: EntityRelationSongAlbum)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertRelationSongAlbum(list: List<EntityRelationSongAlbum>)


    // Relation Song Artist
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertRelationSongArtist(relationSongArtist: EntityRelationSongArtist)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertRelationSongArtist(list: List<EntityRelationSongArtist>)


    // Relation Song Country
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertRelationSongCountry(relationSongCountry: EntityRelationSongCountry)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertRelationSongCountry(list: List<EntityRelationSongCountry>)


    // Relation Song Playlist
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertRelationSongPlaylist(relationSongPlaylist: EntityRelationSongPlaylist)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertRelationSongPlaylist(list: List<EntityRelationSongPlaylist>)

    // Relation Suggested Song by Artist
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertRelationSuggestedSongByArtist(relationPromotedSongByArtist: EntityRelationSuggestedSongByArtist)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertRelationSuggestedSongByArtist(list: List<EntityRelationSuggestedSongByArtist>)

    // Relation with artist
    // ---------------------------------------------------------------------------------------------

    // Relation Artist Album
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertRelationArtistAlbum(relationArtistAlbum: EntityRelationArtistAlbum)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertRelationArtistAlbum(list: List<EntityRelationArtistAlbum>)

    // Relation Artist Country
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertRelationArtistCountry(relationSongCountry: EntityRelationArtistCountry)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertRelationArtistCountry(list: List<EntityRelationArtistCountry>)


    // Relation Artist Genre
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertRelationArtistGenre(relationSongCountry: EntityRelationArtistGenre)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertRelationArtistGenre(list: List<EntityRelationArtistGenre>)

    // Explore Type
    // ---------------------------------------------------------------------------------------------

    // Explore
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertExploreType(explore: EntityExplore)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertExploreType(list: List<EntityExplore>)

    // Prev Explore
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPrevExploreType(explore: EntityPrevExplore)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPrevExploreType(list: List<EntityPrevExplore>)
}