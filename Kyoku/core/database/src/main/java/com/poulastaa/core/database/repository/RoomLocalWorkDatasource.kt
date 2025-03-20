package com.poulastaa.core.database.repository

import com.poulastaa.core.database.dao.RootDao
import com.poulastaa.core.database.dao.WorkDao
import com.poulastaa.core.database.entity.EntityFavourite
import com.poulastaa.core.database.entity.EntityRelationArtistCountry
import com.poulastaa.core.database.entity.EntityRelationArtistGenre
import com.poulastaa.core.database.entity.EntityRelationSongAlbum
import com.poulastaa.core.database.entity.EntityRelationSongArtist
import com.poulastaa.core.database.entity.EntityRelationSongPlaylist
import com.poulastaa.core.database.entity.EntitySavedAlbum
import com.poulastaa.core.database.entity.EntitySavedArtist
import com.poulastaa.core.database.mapper.toEntityAlbum
import com.poulastaa.core.database.mapper.toEntityArtist
import com.poulastaa.core.database.mapper.toEntityCountry
import com.poulastaa.core.database.mapper.toEntityGenre
import com.poulastaa.core.database.mapper.toEntityPlaylist
import com.poulastaa.core.database.mapper.toEntitySong
import com.poulastaa.core.database.mapper.toSongInfo
import com.poulastaa.core.domain.model.AlbumId
import com.poulastaa.core.domain.model.ArtistId
import com.poulastaa.core.domain.model.DtoArtist
import com.poulastaa.core.domain.model.DtoFullAlbum
import com.poulastaa.core.domain.model.DtoFullPlaylist
import com.poulastaa.core.domain.model.DtoSong
import com.poulastaa.core.domain.model.PlaylistId
import com.poulastaa.core.domain.model.SongId
import com.poulastaa.core.domain.repository.LocalWorkDatasource
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

internal class RoomLocalWorkDatasource @Inject constructor(
    private val work: WorkDao,
    private val root: RootDao,
) : LocalWorkDatasource {
    // album
    override suspend fun getSavedAlbumIds(): List<AlbumId> = work.getAllSavedAlbumIds()

    override suspend fun removeSavedAlbums(list: List<AlbumId>) =
        work.removeSavedAlbums(list.map { EntitySavedAlbum(it) })

    override suspend fun saveAlbums(list: List<DtoFullAlbum>) {
        coroutineScope {
            val albumDef = async { root.insertAlbum(list.map { it.album.toEntityAlbum() }) }
            val song = async { insertSongs(list.map { it.songs }.flatten()) }

            listOf(
                albumDef,
                song
            ).awaitAll()

            val songAlbumRelation = async {
                root.insertRelationSongAlbum(
                    list.map { dto ->
                        dto.songs.map {
                            EntityRelationSongAlbum(
                                songId = it.id,
                                albumId = dto.album.id
                            )
                        }
                    }.flatten()
                )
            }
            val savedAlbum = async {
                root.insertSavedAlbum(list.map { EntitySavedAlbum(it.album.id) })
            }

            listOf(
                songAlbumRelation,
                savedAlbum
            ).awaitAll()
        }
    }

    // playlist
    override suspend fun getSavedPlaylistIds(): List<PlaylistId> = work.getAllSavedPlaylistIds()

    override suspend fun removeSavedPlaylists(list: List<PlaylistId>) =
        work.removeSavedPlaylists(list)

    override suspend fun savePlaylists(list: List<DtoFullPlaylist>) {
        coroutineScope {
            val playlistDef = async {
                root.insertPlaylist(list.map { it.playlist.toEntityPlaylist() })
            }
            val song = async { insertSongs(list.map { it.songs }.flatten()) }

            listOf(
                playlistDef,
                song
            ).awaitAll()

            root.insertRelationSongPlaylist(
                list.map { dto ->
                    dto.songs.map {
                        EntityRelationSongPlaylist(
                            songId = it.id,
                            playlistId = dto.playlist.id
                        )
                    }
                }.flatten()
            )
        }
    }

    // artist
    override suspend fun getSavedArtistIds(): List<ArtistId> = work.getAllSavedArtistIds()

    override suspend fun removeSavedArtists(list: List<ArtistId>) =
        work.removeSavedArtists(list.map { EntitySavedArtist(it) })

    override suspend fun saveArtists(list: List<DtoArtist>) {
        root.insertArtist(list.map { it.toEntityArtist() }).also {
            root.insertSavedArtist(list.map { EntitySavedArtist(it.id) })
        }
    }

    // favourite
    override suspend fun getSavedFavouriteIds(): List<SongId> = work.getAllSavedFavouriteSongIds()

    override suspend fun removeSavedFavourites(list: List<SongId>) =
        work.removeSavedFavouriteSongs(list)

    override suspend fun saveFavourites(list: List<DtoSong>) {
        insertSongs(list).also {
            root.insertFavourite(list.map { EntityFavourite(it.id) })
        }
    }

    private suspend fun insertSongs(list: List<DtoSong>) {
        coroutineScope {
            val songDef = async { root.insertSong(list.map { it.toEntitySong() }) }
            val artistDef = async {
                root.insertArtist(list.map { it.artist }.flatten().map { it.toEntityArtist() })
            }
            val albumDef = async {
                root.insertAlbum(list.map { it.album?.toEntityAlbum() }.mapNotNull { it })
            }
            val infoDef = async { root.insertSongInfo(list.map { it.info.toSongInfo() }) }
            val countryDef = async {
                root.insertCountry(
                    list.map { list ->
                        list.artist.map {
                            it.country?.toEntityCountry()
                        }
                    }.flatten().mapNotNull { it }
                )
            }
            val genreDef = async {
                root.insertGenre(
                    list.map { list ->
                        list.artist.map { it.genre?.toEntityGenre() }
                    }.flatten().mapNotNull { it }
                )
            }

            listOf(
                songDef,
                artistDef,
                albumDef,
                infoDef,
                countryDef,
                genreDef
            ).awaitAll()

            val songAlbumRelationDef = async {
                root.insertRelationSongAlbum(
                    list.map { song ->
                        song.album?.let {
                            EntityRelationSongAlbum(
                                songId = song.id,
                                albumId = it.id
                            )
                        }
                    }.mapNotNull { it }
                )
            }

            val songArtistRelationDef = async {
                root.insertRelationSongArtist(
                    list.map { song ->
                        song.artist.map { artist ->
                            EntityRelationSongArtist(
                                songId = song.id,
                                artistId = artist.id
                            )
                        }
                    }.flatten()
                )
            }

            val artist = list.map { it.artist }.flatten()
            val artistCountryRelationDef = async {
                root.insertRelationArtistCountry(
                    artist.map { dto ->
                        dto.country?.let {
                            EntityRelationArtistCountry(
                                artistId = dto.id,
                                countryId = it.id
                            )
                        }
                    }.mapNotNull { it }
                )
            }
            val artistGenreRelationDef = async {
                root.insertRelationArtistGenre(
                    artist.map { dto ->
                        dto.genre?.let {
                            EntityRelationArtistGenre(
                                artistId = dto.id,
                                genreId = it.id
                            )
                        }
                    }.mapNotNull { it }
                )
            }

            listOf(
                songAlbumRelationDef,
                songArtistRelationDef,
                artistCountryRelationDef,
                artistGenreRelationDef,
            ).awaitAll()
        }
    }
}