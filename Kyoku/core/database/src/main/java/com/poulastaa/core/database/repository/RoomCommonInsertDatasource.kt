package com.poulastaa.core.database.repository

import com.poulastaa.core.database.dao.RootDao
import com.poulastaa.core.database.entity.EntityRelationArtistCountry
import com.poulastaa.core.database.entity.EntityRelationArtistGenre
import com.poulastaa.core.database.entity.EntityRelationSongAlbum
import com.poulastaa.core.database.mapper.toEntityAlbum
import com.poulastaa.core.database.mapper.toEntityArtist
import com.poulastaa.core.database.mapper.toEntityCountry
import com.poulastaa.core.database.mapper.toEntityGenre
import com.poulastaa.core.database.mapper.toEntitySong
import com.poulastaa.core.database.mapper.toSongInfo
import com.poulastaa.core.domain.model.DtoSong
import com.poulastaa.core.domain.model.SongId
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

internal class RoomCommonInsertDatasource @Inject constructor(
    private val root: RootDao,
) {
    suspend fun insertSong(list: List<DtoSong>): List<SongId> = coroutineScope {
        val songDef = async { root.insertSong(list.map { it.toEntitySong() }) }
        val artistDef = async {
            root.insertArtist(list.map { dto -> dto.artist.map { it.toEntityArtist() } }
                .flatten())
        }

        val song = songDef.await()
        artistDef.await()

        list.map { dto ->
            async {
                val infoDef = async { root.insertSongInfo(dto.info.toSongInfo()) }
                val albumDef = async {
                    dto.album?.toEntityAlbum()?.let { root.insertAlbum(it) }
                }

                val genre = async {
                    dto.artist.map { it.genre?.toEntityGenre() }
                        .mapNotNull { it }
                        .let { root.insertGenre(it) }
                }
                val country = async {
                    dto.artist.map { it.country?.toEntityCountry() }
                        .mapNotNull { it }
                        .let { root.insertCountry(it) }
                }


                listOf(
                    infoDef,
                    albumDef,
                    genre,
                    country
                ).awaitAll()

                val albumSongRelationDef = dto.album?.id?.let {
                    async {
                        root.insertRelationSongAlbum(
                            EntityRelationSongAlbum(
                                songId = dto.id,
                                albumId = it
                            )
                        )
                    }
                }
                val artistRelation = dto.artist.map { artist ->
                    async {
                        val countryDef = async {
                            artist.country?.let {
                                root.insertRelationArtistCountry(
                                    EntityRelationArtistCountry(
                                        artistId = artist.id,
                                        countryId = it.id
                                    )
                                )
                            }
                        }

                        val genreDef = async {
                            artist.genre?.let {
                                root.insertRelationArtistGenre(
                                    EntityRelationArtistGenre(
                                        artistId = artist.id,
                                        genreId = it.id
                                    )
                                )
                            }
                        }

                        listOf(
                            countryDef,
                            genreDef
                        ).awaitAll()
                    }
                }

                albumSongRelationDef?.await()
                artistRelation.map { it.await() }
            }
        }.awaitAll()

        song
    }
}