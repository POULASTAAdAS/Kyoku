package com.poulastaa.core.database.repository

import com.poulastaa.core.database.dao.RootDao
import com.poulastaa.core.database.entity.EntityRelationArtistCountry
import com.poulastaa.core.database.entity.EntityRelationArtistGenre
import com.poulastaa.core.database.entity.EntityRelationSongAlbum
import com.poulastaa.core.database.entity.EntityRelationSongArtist
import com.poulastaa.core.database.mapper.toEntityAlbum
import com.poulastaa.core.database.mapper.toEntityArtist
import com.poulastaa.core.database.mapper.toEntityCountry
import com.poulastaa.core.database.mapper.toEntityGenre
import com.poulastaa.core.database.mapper.toEntitySong
import com.poulastaa.core.database.mapper.toSongInfo
import com.poulastaa.core.domain.model.DtoSong
import com.poulastaa.core.domain.model.PlaylistId
import com.poulastaa.core.domain.repository.LocalAddSongToPlaylistDatasource
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

internal class RoomLocalAddSongToPlaylistDatasource @Inject constructor(
    private val rootDao: RootDao,
) : LocalAddSongToPlaylistDatasource {
    override suspend fun saveSong(
        playlistId: PlaylistId,
        song: DtoSong,
    ) {
        coroutineScope {
            val songDef = async { rootDao.insertSong(song.toEntitySong()) }
            val albumDef = async { song.album?.toEntityAlbum()?.let { rootDao.insertAlbum(it) } }
            val artistDef = async { rootDao.insertArtist(song.artist.map { it.toEntityArtist() }) }
            val countryDef = async {
                song.artist.map { it.country }.mapNotNull { it?.toEntityCountry() }.let {
                    rootDao.insertCountry(it)
                }
            }
            val genreDef = async {
                song.artist.map { it.genre?.toEntityGenre() }.mapNotNull { it }.let {
                    rootDao.insertGenre(it)
                }
            }

            listOf(
                songDef,
                albumDef,
                artistDef,
                countryDef,
                genreDef
            ).awaitAll()

            val infoDef = async { rootDao.insertSongInfo(song.info.toSongInfo()) }
            val songAlbumRelation = async {
                song.album?.let {
                    rootDao.insertRelationSongAlbum(
                        EntityRelationSongAlbum(
                            songId = song.id,
                            albumId = it.id
                        )
                    )
                }
            }
            val songArtistRelation = async {
                song.artist.map {
                    EntityRelationSongArtist(
                        songId = song.id,
                        artistId = it.id
                    )
                }.let { rootDao.insertRelationSongArtist(it) }
            }
            val artistCountryRelation = async {
                song.artist.map { dto ->
                    dto.country?.let {
                        EntityRelationArtistCountry(
                            artistId = dto.id,
                            countryId = it.id
                        )
                    }
                }.mapNotNull { it }.let { rootDao.insertRelationArtistCountry(it) }
            }
            val artistGeneRelation = async {
                song.artist.map { dto ->
                    dto.genre?.let {
                        EntityRelationArtistGenre(
                            artistId = dto.id,
                            genreId = it.id
                        )
                    }
                }.mapNotNull { it }.let { rootDao.insertRelationArtistGenre(it) }
            }

            listOf(
                infoDef,
                songAlbumRelation,
                songArtistRelation,
                artistCountryRelation,
                artistGeneRelation
            ).awaitAll()
        }
    }
}