package com.poulastaa.core.database.repository

import com.poulastaa.core.database.dao.ImportPlaylistDao
import com.poulastaa.core.database.dao.RootDao
import com.poulastaa.core.database.entity.EntityRelationArtistCountry
import com.poulastaa.core.database.entity.EntityRelationArtistGenre
import com.poulastaa.core.database.entity.EntityRelationSongAlbum
import com.poulastaa.core.database.entity.EntityRelationSongArtist
import com.poulastaa.core.database.entity.EntityRelationSongPlaylist
import com.poulastaa.core.database.mapper.toDtoPrevPlaylist
import com.poulastaa.core.database.mapper.toEntityAlbum
import com.poulastaa.core.database.mapper.toEntityArtist
import com.poulastaa.core.database.mapper.toEntityCountry
import com.poulastaa.core.database.mapper.toEntityGenre
import com.poulastaa.core.database.mapper.toEntityPlaylist
import com.poulastaa.core.database.mapper.toEntitySong
import com.poulastaa.core.database.mapper.toSongInfo
import com.poulastaa.core.domain.model.DtoFullPlaylist
import com.poulastaa.core.domain.model.DtoPrevPlaylist
import com.poulastaa.core.domain.repository.LocalImportPlaylistDatasource
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RoomLocalImportPlaylistDatasource @Inject constructor(
    private val root: RootDao,
    private val local: ImportPlaylistDao,
) : LocalImportPlaylistDatasource {
    override fun loadAllPlaylist(): Flow<List<DtoPrevPlaylist>> {
        return local.getAllPlaylist().map { list ->
            list.map { song ->
                val releaseYear = local.getSongInfo(song.list.map { it.id })
                    .associate { it.songId to it.releaseYear }
                val artists = local.getArtistNameOnSongId(song.list.map { it.id })

                song.toDtoPrevPlaylist(artists, releaseYear)
            }
        }
    }

    override suspend fun storePlaylist(playlist: DtoFullPlaylist) {
        coroutineScope {
            val songs = playlist.songs
            val songGenre = songs.mapNotNull { song ->
                song.genre?.let {
                    song.id to it.toEntityGenre()
                }
            }
            val songAlbum = songs.mapNotNull { song ->
                song.album?.let {
                    song.id to it
                }
            }

            val artist = songs.map { it.artist }.flatten()
            val artistGenre = artist.mapNotNull { dtoArtist ->
                dtoArtist.genre?.let {
                    dtoArtist.id to it.toEntityGenre()
                }
            }
            val artistCountry = artist.mapNotNull { dtoArtist ->
                dtoArtist.country?.let {
                    dtoArtist.id to it.toEntityCountry()
                }
            }

            // Primary entry
            val playlistDef = async { root.insertPlaylist(playlist.playlist.toEntityPlaylist()) }
            val songDef = async { root.insertSong(songs.map { it.toEntitySong() }) }
            val artistDef = async { root.insertArtist(artist.map { it.toEntityArtist() }) }
            val genreDef = async {
                root.insertGenre((songGenre + artistGenre).distinctBy { it.second.id }
                    .map { it.second })
            }
            val albumDef = async { root.insertAlbum(songAlbum.map { it.second.toEntityAlbum() }) }
            val countryDef = async { root.insertCountry(artistCountry.map { it.second }) }

            // Relation entry
            val songInfoDef = async { root.insertSongInfo(songs.map { it.info.toSongInfo() }) }
            val songPlaylistDef = async {
                root.insertRelationSongPlaylist(
                    songs.map { song ->
                        EntityRelationSongPlaylist(
                            songId = song.id,
                            playlistId = playlist.playlist.id
                        )
                    }
                )
            }
            val songArtistDef = async {
                root.insertRelationSongArtist(
                    songs.map { song ->
                        song.artist.map {
                            EntityRelationSongArtist(
                                songId = song.id,
                                artistId = it.id
                            )
                        }
                    }.flatten()
                )
            }
            val songAlbumDef = async {
                root.insertRelationSongAlbum(
                    songAlbum.map { it.first to it.second.id }
                        .map { (songId, albumId) ->
                            EntityRelationSongAlbum(
                                songId = songId,
                                albumId = albumId
                            )
                        }
                )
            }
            val artistGenreDef = async {
                root.insertRelationArtistGenre(
                    artistGenre.map {
                        EntityRelationArtistGenre(
                            artistId = it.first,
                            genreId = it.second.id
                        )
                    }
                )
            }
            val artistCountryDef = async {
                root.insertRelationArtistCountry(
                    artistCountry.map {
                        EntityRelationArtistCountry(
                            artistId = it.first,
                            countryId = it.second.id
                        )
                    }
                )
            }

            // Primary entry
            listOf(
                playlistDef,
                songDef,
                artistDef,
                genreDef,
                albumDef,
                countryDef
            ).awaitAll()

            // Relation entry
            listOf(
                songInfoDef,
                songPlaylistDef,
                songArtistDef,
                songAlbumDef,
                artistGenreDef,
                artistCountryDef
            ).awaitAll()
        }
    }
}