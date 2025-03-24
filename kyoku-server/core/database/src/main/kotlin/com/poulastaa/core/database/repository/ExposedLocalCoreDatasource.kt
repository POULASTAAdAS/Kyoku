package com.poulastaa.core.database.repository

import com.poulastaa.core.database.SQLDbManager.kyokuDbQuery
import com.poulastaa.core.database.SQLDbManager.userDbQuery
import com.poulastaa.core.database.dao.*
import com.poulastaa.core.database.entity.app.*
import com.poulastaa.core.database.entity.user.EntityUser
import com.poulastaa.core.database.entity.user.RelationEntityUserPlaylist
import com.poulastaa.core.database.mapper.*
import com.poulastaa.core.domain.model.*
import com.poulastaa.core.domain.repository.*
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
        val playlist = dbPlaylist.toDtoPlaylist()
        cache.setPlaylistOnId(playlist)

        return playlist
    }

    override suspend fun getArtistOnSongId(list: List<SongId>): List<Pair<SongId, List<DtoArtist>>> = coroutineScope {
        val cacheSongIdArtistIdList = cache.cacheArtistIdBySongId(list)

        if (cacheSongIdArtistIdList.isEmpty()) getArtistsOnSongId(list)
        else {
            val notFoundSongIdList = list.filterNot { it in cacheSongIdArtistIdList.keys }
            val dbResult = getArtistsOnSongId(notFoundSongIdList)

            cacheSongIdArtistIdList.map { (songId, artistLis) ->
                if (artistLis.isNotEmpty()) {
                    val cacheRes = cache.cacheArtistById(artistLis)
                    if (cacheRes.isEmpty()) getArtistsOnSongId(songId)
                    else songId to cacheRes
                } else songId to emptyList()
            } + dbResult
        }

        getArtistsOnSongId(list)
    }

    override suspend fun getInfoOnSongId(list: List<SongId>): List<Pair<SongId, DtoSongInfo>> {
        val cacheInfo = cache.cacheSongInfo(list).associateBy { it.id }

        val missingSongIds = list.filterNot { it in cacheInfo.keys }
        val dbInfo = if (missingSongIds.isNotEmpty()) kyokuDbQuery {
            EntitySongInfo.selectAll().where {
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
                        ).select(
                            EntityGenre.id,
                            EntityGenre.genre,
                            EntityGenre.popularity,
                            RelationEntitySongGenre.songId
                        )
                        .where {
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
                                .firstOrNull()?.toDtoAlbum()?.also { cache.setAlbumById(it) }
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
                        ).select(
                            EntityAlbum.id,
                            EntityAlbum.name,
                            EntityAlbum.popularity,
                            RelationEntitySongAlbum.songId
                        )
                        .where {
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

    override suspend fun getSongOnId(songId: SongId): DtoSong? {
        TODO("Not yet implemented")
    }

    override suspend fun getSongOnId(list: List<SongId>): List<DtoSong> = coroutineScope {
        val cache = cache.cacheSongById(list)
        val ids = cache.map { it.id }
        val notFoundSongs = list.filterNot { ids.contains(it) }

        val daoSongs = kyokuDbQuery {
            DaoSong.find {
                EntitySong.id inList notFoundSongs
            }.toList()
        }

        val idList = kyokuDbQuery { daoSongs.map { it.id.value as SongId } }

        val albumDef = async { getAlbumOnSongId(idList) }
        val artistDef = async { getArtistOnSongId(idList) }
        val infoDef = async { getInfoOnSongId(idList) }
        val genreDef = async { getGenreOnSongId(idList) }

        val artist = artistDef.await()
        val album = albumDef.await()
        val info = infoDef.await()
        val genre = genreDef.await()

        daoSongs.map { song ->
            song.toSongDto(
                artist = artist.firstOrNull { it.first == song.id.value }?.second ?: emptyList(),
                album = album.firstOrNull { it.first == song.id.value }?.second?.copy(rawPoster = song.poster),
                info = info.firstOrNull { it.first == song.id.value }?.second ?: DtoSongInfo(),
                genre = genre.firstOrNull { it.first == song.id.value }?.second,
            )
        }.also { this@ExposedLocalCoreDatasource.cache.setSongById(it) }
    }

    override suspend fun getDetailedPrevSongOnId(songId: SongId): DtoDetailedPrevSong? {
        TODO("Not yet implemented")
    }

    override suspend fun getDetailedPrevSongOnId(list: List<SongId>): List<DtoDetailedPrevSong> = coroutineScope {
        kyokuDbQuery {
            DaoSong.find {
                EntitySong.id inList list
            }.map { it.toDtoPrevSong() }
        }.map {
            val artistDef = async { getArtistOnSongId(listOf(it.id)).firstOrNull()?.second?.map { it.name } }
            val infoDef = async { getInfoOnSongId(listOf(it.id)).map { it.second.releaseYear }.firstOrNull() }

            it.toDetailedPrevSong(
                artist = artistDef.await()?.joinToString(","),
                releaseYear = infoDef.await() ?: -1
            )
        }.also {
            cache.setDetailedPrevSongById(it)
        }
    }

    override suspend fun getArtistFromDbArtist(list: List<DtoDBArtist>): List<DtoArtist> = coroutineScope {
        list.map { artist ->
            async {
                getArtist(artist)
            }
        }.awaitAll().also { cache.setArtistById(it) }
    }

    override suspend fun getGenreOnArtistId(artistId: ArtistId): DtoGenre? =
        cache.cacheGenreIdByArtistId(artistId)?.let { cache.cacheGenreById(it) } ?: kyokuDbQuery {
            EntityGenre
                .join(
                    otherTable = RelationEntityArtistGenre,
                    onColumn = EntityGenre.id,
                    otherColumn = RelationEntityArtistGenre.genreId,
                    joinType = JoinType.INNER
                )
                .select(
                    EntityGenre.id,
                    EntityGenre.genre,
                    EntityGenre.popularity
                ).where {
                    RelationEntityArtistGenre.artistId eq artistId
                }.firstOrNull()?.let {
                    DtoGenre(
                        id = it[EntityGenre.id].value,
                        name = it[EntityGenre.genre],
                        popularity = it[EntityGenre.popularity]
                    )
                }
        }.also {
            if (it != null) {
                cache.setGenreById(it)
                cache.setGenreIdByArtistId(artistId, it.id)
            }
        }

    override suspend fun getCountryOnArtistId(artistId: ArtistId): DtoCountry? =
        cache.cacheCountryIdByArtistId(artistId)?.let { cache.cacheCountryById(it) } ?: kyokuDbQuery {
            EntityCountry
                .join(
                    otherTable = RelationEntityArtistCountry,
                    onColumn = EntityCountry.id,
                    otherColumn = RelationEntityArtistCountry.countryId,
                    joinType = JoinType.INNER
                )
                .select(
                    EntityCountry.id,
                    EntityCountry.country,
                ).where {
                    RelationEntityArtistCountry.artistId eq artistId
                }.firstOrNull()?.let {
                    DtoCountry(
                        id = it[EntityCountry.id].value,
                        name = it[EntityCountry.country],
                    )
                }
        }.also {
            if (it != null) {
                cache.setCountryById(it)
                cache.setCountryIdByArtistId(artistId, it.id)
            }
        }

    override suspend fun getAlbumOnId(list: List<AlbumId>): List<DtoAlbum> {
        val cache = cache.cacheAlbumById(list)
        val notFound = list.filter { it !in cache.map { it.id } }

        return cache + kyokuDbQuery {
            DaoAlbum.find {
                EntityAlbum.id inList notFound
            }.map { it.toDtoAlbum() }.also {
                this.cache.setAlbumById(it)
            }
        }
    }

    override suspend fun getPlaylistOnId(list: List<PlaylistId>): List<DtoPlaylist> {
        val cache = cache.cachePlaylistOnId(list)
        val notFound = list.filter { it !in cache.map { it.id } }

        return cache + kyokuDbQuery {
            DaoPlaylist.find {
                EntityPlaylist.id inList notFound
            }.map { it.toDtoPlaylist() }.also {
                this.cache.setPlaylistOnId(it)
            }
        }
    }

    override suspend fun getArtistOnId(list: List<ArtistId>): List<DtoArtist> {
        val cache = cache.cacheArtistById(list)
        val notFoundArtistIdList = list.filter { it !in cache.map { it.id } }

        return cache + coroutineScope {
            kyokuDbQuery {
                DaoArtist.find { EntityArtist.id inList notFoundArtistIdList }.map { it.toDbArtistDto() }
            }.map {
                async {
                    getArtist(it).also {
                        this@ExposedLocalCoreDatasource.cache.setArtistById(it)
                    }
                }
            }.awaitAll()
        }
    }

    private suspend fun getArtistsOnSongId(songId: SongId): Pair<SongId, List<DtoArtist>> {
        val pair = songId to kyokuDbQuery {
            EntityArtist
                .join(
                    otherTable = RelationEntitySongArtist,
                    onColumn = EntityArtist.id,
                    otherColumn = RelationEntitySongArtist.artistId,
                    joinType = JoinType.INNER
                ).select(
                    EntityArtist.id,
                    EntityArtist.name,
                    EntityArtist.coverImage,
                    EntityArtist.popularity
                ).where {
                    RelationEntitySongArtist.songId eq songId
                }.map { resultRow ->
                    DtoDBArtist(
                        id = resultRow[EntityArtist.id].value,
                        name = resultRow[EntityArtist.name],
                        coverImage = resultRow[EntityArtist.coverImage],
                        popularity = resultRow[EntityArtist.popularity]
                    )
                }
        }

        return convertDbArtistToDtoArtist(listOf(pair)).first()
    }

    private suspend fun getArtistsOnSongId(songIdList: List<SongId>): List<Pair<SongId, List<DtoArtist>>> {
        val dbResult = kyokuDbQuery {
            EntityArtist
                .join(
                    otherTable = RelationEntitySongArtist,
                    onColumn = EntityArtist.id,
                    otherColumn = RelationEntitySongArtist.artistId,
                    joinType = JoinType.INNER
                ).select(
                    EntityArtist.id,
                    EntityArtist.name,
                    EntityArtist.coverImage,
                    EntityArtist.popularity,
                    RelationEntitySongArtist.songId
                ).where {
                    RelationEntitySongArtist.songId inList songIdList
                }.groupBy {
                    it[RelationEntitySongArtist.songId]
                }.map { (songId: SongId, resultList) ->
                    songId to if (resultList.isEmpty()) emptyList()
                    else {
                        resultList.map { resultRow ->
                            DtoDBArtist(
                                id = resultRow[EntityArtist.id].value,
                                name = resultRow[EntityArtist.name],
                                coverImage = resultRow[EntityArtist.coverImage],
                                popularity = resultRow[EntityArtist.popularity]
                            )
                        }
                    }
                }
        }

        return convertDbArtistToDtoArtist(dbResult)
    }

    private suspend fun convertDbArtistToDtoArtist(dbResult: List<Pair<SongId, List<DtoDBArtist>>>) = coroutineScope {
        dbResult.map { (songId, dbArtistList) ->
            async {
                songId to dbArtistList.map { dbArtist ->
                    async {
                        getArtist(dbArtist)
                    }
                }.awaitAll()
            }
        }.awaitAll().onEach { pair ->
            if (pair.second.isNotEmpty()) {
                cache.setArtistById(pair.second)
                cache.setArtistIdBySongId(pair.first, pair.second.map { it.id })
            }
        }
    }

    private suspend fun getArtist(dbArtist: DtoDBArtist): DtoArtist {
        val genre = getGenreOnArtistId(dbArtist.id)
        val country = getCountryOnArtistId(dbArtist.id)

        return dbArtist.toArtistDto(
            genre = genre,
            country = country
        )
    }
}