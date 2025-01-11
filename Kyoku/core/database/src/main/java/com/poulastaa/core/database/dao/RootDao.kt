package com.poulastaa.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.poulastaa.core.database.entity.EntityAlbum
import com.poulastaa.core.database.entity.EntityArtist
import com.poulastaa.core.database.entity.EntityCountry
import com.poulastaa.core.database.entity.EntityFavourite
import com.poulastaa.core.database.entity.EntityGenre
import com.poulastaa.core.database.entity.EntityPlaylist
import com.poulastaa.core.database.entity.EntityRelationArtistAlbum
import com.poulastaa.core.database.entity.EntityRelationArtistCountry
import com.poulastaa.core.database.entity.EntityRelationArtistGenre
import com.poulastaa.core.database.entity.EntityRelationSongAlbum
import com.poulastaa.core.database.entity.EntityRelationSongArtist
import com.poulastaa.core.database.entity.EntityRelationSongCountry
import com.poulastaa.core.database.entity.EntityRelationSongPlaylist
import com.poulastaa.core.database.entity.EntityRelationSuggestedPlaylist
import com.poulastaa.core.database.entity.EntityRelationSuggestedSongByArtist
import com.poulastaa.core.database.entity.EntitySong
import com.poulastaa.core.database.entity.EntitySongInfo
import com.poulastaa.core.database.entity.EntitySuggestedAlbum
import com.poulastaa.core.database.entity.EntitySuggestedArtist

@Dao
interface RootDao {
    // Song
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSong(song: EntitySong)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSong(list: List<EntitySong>)


    // Artist
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertArtist(artist: EntityArtist)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertArtist(list: List<EntityArtist>)


    // Album
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAlbum(album: EntityAlbum)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAlbum(list: List<EntityAlbum>)


    // Country
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCountry(country: EntityCountry)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCountry(list: List<EntityCountry>)


    // Genre
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCountry(genre: EntityGenre)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCountry(list: List<EntityGenre>)


    // Playlist
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPlaylist(playlist: EntityPlaylist)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPlaylist(list: List<EntityPlaylist>)


    // Favourite
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFavourite(favourite: EntityFavourite)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFavourite(list: List<EntityFavourite>)

    // ---------------------------------------------------------------------------------------------

    // SongInfo
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSongInfo(songInfo: EntitySongInfo)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSongInfo(list: List<EntitySongInfo>)

    // Suggested
    // ---------------------------------------------------------------------------------------------

    // SuggestedAlbum
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSuggestedAlbum(suggestedAlbum: EntitySuggestedAlbum)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSuggestedAlbum(list: List<EntitySuggestedAlbum>)


    // SuggestedArtist
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSuggestedArtist(suggestedArtist: EntitySuggestedArtist)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSuggestedArtist(list: List<EntitySuggestedArtist>)

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

    // Relation Suggested
    // ---------------------------------------------------------------------------------------------

    // Relation Suggested Playlist
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertRelationSuggestedPlaylist(relationSuggestedPlaylist: EntityRelationSuggestedPlaylist)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertRelationSuggestedPlaylist(list: List<EntityRelationSuggestedPlaylist>)

    // Relation Suggested Song by Artist
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertRelationPromotedSongByArtist(relationPromotedSongByArtist: EntityRelationSuggestedSongByArtist)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertRelationPromotedSongByArtist(list: List<EntityRelationSuggestedSongByArtist>)

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
}