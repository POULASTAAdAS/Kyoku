package com.poulastaa.core.database.repository

import com.poulastaa.core.database.SQLDbManager.kyokuDbQuery
import com.poulastaa.core.database.SQLDbManager.userDbQuery
import com.poulastaa.core.database.dao.*
import com.poulastaa.core.database.entity.app.*
import com.poulastaa.core.database.entity.user.EntityUser
import com.poulastaa.core.database.entity.user.RelationEntityUserPlaylist
import com.poulastaa.core.database.mapper.*
import com.poulastaa.core.domain.model.*
import com.poulastaa.core.domain.repository.LocalCoreCacheDatasource
import com.poulastaa.core.domain.repository.LocalCoreDatasource
import com.poulastaa.core.domain.repository.SongId
import kotlinx.coroutines.*
import org.jetbrains.exposed.sql.*

class ExposedLocalCoreDatasource(
    private val cache: LocalCoreCacheDatasource,
) : LocalCoreDatasource {
    override suspend fun getUserByEmail(email: String, userType: UserType): DtoDBUser? {
        cache.cacheUsersByEmail(email, userType)?.let { return it }

        val dbUser = userDbQuery {
            DaoUser.find {
                EntityUser.email eq email and (EntityUser.userType eq userType.name)
            }.firstOrNull()
        } ?: return null

        cache.setUserByEmail(email, userType, dbUser.toDbUserDto())

        return dbUser.toDbUserDto()
    }

    override suspend fun createPlaylist(userId: Long, list: List<SongId>): DtoPlaylist {
        val totalPlaylistCount = kyokuDbQuery {
            DaoPlaylist.all().count()
        }

        val dbPlaylist = kyokuDbQuery {
            DaoPlaylist.new {
                this.name = "Playlist ${totalPlaylistCount + 1}"
            }
        }

        CoroutineScope(Dispatchers.IO).launch {
            kyokuDbQuery {
                RelationSongPlaylist.batchInsert(data = list, ignore = true, shouldReturnGeneratedValues = false) {
                    this[RelationSongPlaylist.playlistId] = dbPlaylist.id.value
                    this[RelationSongPlaylist.songId] = it
                }
            }

            userDbQuery {
                RelationEntityUserPlaylist.insertIgnore {
                    it[this.userId] = userId
                    it[this.playlistId] = dbPlaylist.id.value
                }
            }
        }
        val playlist = dbPlaylist.toPlaylistDto()
        cache.setPlaylist(playlist)

        return playlist
    }

    override suspend fun getArtistOnSongId(list: List<SongId>): List<Pair<SongId, List<DtoArtist>>> { // todo fix not returning artist
        val dbMap = mutableMapOf<SongId, List<DtoDBArtist>>()

        // Step 1: Cache lookup and resolve missing artist IDs
        val cacheIdList = cache.cacheArtistIdBySongId(list)
        val cacheSongIdDtoArtist = cacheIdList.map { (songId, artistIds) ->
            if (artistIds.isNotEmpty()) {
                val artist = cache.cacheArtistById(artistIds)

                val missingArtistIds = artistIds.filterNot { id -> artist.any { it.id == id } }
                if (missingArtistIds.isNotEmpty()) {
                    val dbArtists = kyokuDbQuery {
                        DaoArtist.find { EntityArtist.id inList missingArtistIds }
                    }.map { it.toDbArtistDto() }

                    dbMap[songId] = dbArtists
                }

                songId to artist
            } else songId to emptyList()
        }

        println("list: $list")
        println("list: $list")
        println("list: $list")
        println("list: $list")

        println("cacheIdList: $cacheIdList")
        println("cacheIdList: $cacheIdList")
        println("cacheIdList: $cacheIdList")
        println("cacheIdList: $cacheIdList")

        // Step 2: get artists for missing song IDs
        val missingSongIds = list.filterNot { it in cacheIdList.keys }

        println("missingSongIds: $missingSongIds")
        println("missingSongIds: $missingSongIds")
        println("missingSongIds: $missingSongIds")
        println("missingSongIds: $missingSongIds")

        if (missingSongIds.isNotEmpty()) kyokuDbQuery {
            EntityArtist.join(
                otherTable = RelationEntitySongArtist,
                onColumn = EntityArtist.id,
                otherColumn = RelationEntitySongArtist.artistId,
                joinType = JoinType.INNER
            ).slice(
                EntityArtist.id,
                EntityArtist.name,
                EntityArtist.coverImage,
                EntityArtist.popularity,
                RelationEntitySongArtist.songId
            ).select {
                RelationEntitySongArtist.songId inList missingSongIds
            }.groupBy { it[RelationEntitySongArtist.songId] }.map {
                dbMap[it.key] = it.value.map {
                    DtoDBArtist(
                        id = it[EntityArtist.id].value,
                        name = it[EntityArtist.name],
                        coverImage = it[EntityArtist.coverImage],
                        popularity = it[EntityArtist.popularity]
                    )
                }
            }
        }

        println("dbMap: $dbMap")
        println("dbMap: $dbMap")
        println("dbMap: $dbMap")
        println("dbMap: $dbMap")
        println("dbMap: $dbMap")

        // Step 3: Convert DB artists to final DTOs concurrently
        val dbSongIdDtoArtist = dbMap.map { (songId, artistList) ->
            songId to artistList.map { artist ->
                val genre = cache.cacheGenreIdByArtistId(artist.id)?.let { cache.cacheGenreById(it) }
                    ?: kyokuDbQuery {
                        EntityGenre.join(
                            RelationEntityArtistGenre,
                            JoinType.INNER,
                            EntityGenre.id,
                            RelationEntityArtistGenre.genreId
                        ).slice(
                            EntityGenre.id,
                            EntityGenre.genre,
                            EntityGenre.popularity
                        ).select {
                            RelationEntityArtistGenre.artistId eq artist.id
                        }.firstOrNull()?.let {
                            DtoGenre(
                                id = it[EntityGenre.id].value,
                                name = it[EntityGenre.genre],
                                popularity = it[EntityGenre.popularity]
                            )
                        }
                    }

                val country = cache.cacheCountryIdByArtistId(artist.id)?.let { cache.cacheCountryById(it) }
                    ?: kyokuDbQuery {
                        EntityCountry.join(
                            RelationEntityArtistCountry,
                            JoinType.INNER,
                            EntityCountry.id,
                            RelationEntityArtistCountry.countryId
                        ).slice(
                            EntityCountry.id,
                            EntityCountry.country
                        ).select {
                            EntityArtist.id eq artist.id
                        }.firstOrNull()?.let {
                            DtoCountry(
                                id = it[EntityCountry.id].value,
                                name = it[EntityCountry.country]
                            )
                        }
                    }

                // set cache
                if (genre != null) {
                    cache.setGenreIdByArtistId(artist.id, genre.id)
                    cache.setGenreById(genre)
                }
                if (country != null) {
                    cache.setCountryIdByArtistId(artist.id, country.id)
                    cache.setCountryById(country)
                }

                artist.toArtistDto(
                    genre = genre ?: DtoGenre(),
                    country = country ?: DtoCountry()
                )
            }
        }

        val songIdToArtistDto = cacheSongIdDtoArtist + dbSongIdDtoArtist

        // set cache
        songIdToArtistDto.forEach { (songId, artistList) ->
            cache.setArtistIdBySongId(songId, artistList.map { it.id })
            cache.setArtistById(artistList)
        }

        return songIdToArtistDto
    }

    override suspend fun getInfoOnSongId(list: List<SongId>): List<Pair<SongId, DtoSongInfo>> {
        val cacheInfo = cache.cacheSongInfo(list).associateBy { it.id }

        val missingSongIds = list.filterNot { it in cacheInfo.keys }
        val dbInfo = if (missingSongIds.isNotEmpty()) kyokuDbQuery {
            EntitySongInfo.select {
                EntitySongInfo.songId inList missingSongIds
            }.map {
                it[EntitySongInfo.songId].value to DtoSongInfo(
                    id = it[EntitySongInfo.songId].value,
                    popularity = it[EntitySongInfo.popularity],
                    releaseYear = it[EntitySongInfo.releaseYear],
                    composer = it[EntitySongInfo.composer],
                )
            }
        }.also { info -> cache.setSongInfoById(info.map { it.second }) }
        else emptyList()

        return cacheInfo.map { it.key to it.value } + dbInfo
    }

    override suspend fun getGenreOnSongId(list: List<SongId>): List<Pair<SongId, DtoGenre>> {
        return coroutineScope {
            val cacheGenreIds = cache.cacheGenreIdBySongId(list)

            val cacheGenreDef = async {
                cacheGenreIds.map { (songId, genreId) ->
                    async {
                        val genre = cache.cacheGenreById(genreId) ?: kyokuDbQuery {
                            DaoGenre.find { EntityGenre.id eq genreId }
                                .firstOrNull()?.toGenreDto()?.also { cache.setGenreById(it) }
                        }

                        genre?.let { songId to it } // Include only non-null genres
                    }
                }.awaitAll().mapNotNull { it }
            }

            val missingSongIds = list.filterNot { it in cacheGenreIds.keys }
            val dbGenreDef = async {
                if (missingSongIds.isNotEmpty()) kyokuDbQuery {
                    EntityGenre
                        .join(
                            otherTable = RelationEntitySongGenre,
                            joinType = JoinType.INNER,
                            onColumn = EntityGenre.id,
                            otherColumn = RelationEntitySongGenre.genreId
                        ).slice(
                            EntityGenre.id,
                            EntityGenre.genre,
                            EntityGenre.popularity,
                            RelationEntitySongGenre.songId
                        )
                        .select {
                            RelationEntitySongGenre.songId inList missingSongIds
                        }.map {
                            it[RelationEntitySongGenre.songId] to DtoGenre(
                                id = it[EntityGenre.id].value,
                                name = it[EntityGenre.genre],
                                popularity = it[EntityGenre.popularity]
                            )
                        }.also {
                            cache.setGenreById(it.toMap().values.toList())
                            cache.setGenreIdBySongId(it.associate { it.first to it.second.id })
                        }
                } else emptyList()
            }

            cacheGenreDef.await() + dbGenreDef.await()
        }
    }

    override suspend fun getAlbumOnSongId(list: List<SongId>): List<Pair<SongId, DtoAlbum>> {
        return coroutineScope {
            val cacheAlbumIds = cache.cacheAlbumIdBySongId(list)

            val cacheAlbumDef = async {
                cacheAlbumIds.map { (songId, albumId) ->
                    async {
                        val album = cache.cacheAlbumById(albumId) ?: kyokuDbQuery {
                            DaoAlbum.find { EntityAlbum.id eq albumId }
                                .firstOrNull()?.toAlbumDto()?.also { cache.setAlbumById(it) }
                        }

                        album?.let { songId to it }
                    }
                }.awaitAll().mapNotNull { it }
            }

            val missingSongIds = list.filterNot { it in cacheAlbumIds.keys }

            val dbAlbumDef = async {
                if (missingSongIds.isNotEmpty()) kyokuDbQuery {
                    EntityAlbum
                        .join(
                            otherTable = RelationEntitySongAlbum,
                            joinType = JoinType.INNER,
                            onColumn = EntityAlbum.id,
                            otherColumn = RelationEntitySongAlbum.albumId
                        ).slice(
                            EntityAlbum.id,
                            EntityAlbum.name,
                            EntityAlbum.popularity,
                            RelationEntitySongAlbum.songId
                        )
                        .select {
                            RelationEntitySongAlbum.songId inList missingSongIds
                        }.map {
                            it[RelationEntitySongAlbum.songId] to DtoAlbum(
                                id = it[EntityAlbum.id].value,
                                name = it[EntityAlbum.name],
                                popularity = it[EntityAlbum.popularity]
                            )
                        }.also {
                            cache.setAlbumById(it.toMap().values.toList())
                            cache.setAlbumIdBySongId(it.associate { it.first to it.second.id })
                        }
                } else emptyList()
            }

            cacheAlbumDef.await() + dbAlbumDef.await()
        }
    }
}