package com.poulastaa.core.database.repository.setup

import com.poulastaa.core.database.SQLDbManager.kyokuDbQuery
import com.poulastaa.core.database.SQLDbManager.shardGenreArtistDbQuery
import com.poulastaa.core.database.SQLDbManager.userDbQuery
import com.poulastaa.core.database.dao.DaoArtist
import com.poulastaa.core.database.dao.DaoGenre
import com.poulastaa.core.database.dao.DaoSong
import com.poulastaa.core.database.dao.DaoUser
import com.poulastaa.core.database.entity.app.EntityArtist
import com.poulastaa.core.database.entity.app.EntityGenre
import com.poulastaa.core.database.entity.app.EntitySong
import com.poulastaa.core.database.entity.app.RelationEntityArtistGenre
import com.poulastaa.core.database.entity.shard.artist_genre.ShardRelationEntityGenreTypeArtist
import com.poulastaa.core.database.entity.user.EntityUser
import com.poulastaa.core.database.entity.user.RelationEntityUserArtist
import com.poulastaa.core.database.entity.user.RelationEntityUserGenre
import com.poulastaa.core.database.mapper.*
import com.poulastaa.core.domain.model.*
import com.poulastaa.core.domain.repository.ArtistId
import com.poulastaa.core.domain.repository.GenreId
import com.poulastaa.core.domain.repository.LocalCoreDatasource
import com.poulastaa.core.domain.repository.setup.LocalSetupCacheDatasource
import com.poulastaa.core.domain.repository.setup.LocalSetupDatasource
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.jetbrains.exposed.sql.*
import java.text.SimpleDateFormat
import java.time.ZoneId
import java.util.*
import kotlin.math.max

class ExposedSetupDatasource(
    private val coreDB: LocalCoreDatasource,
    private val cache: LocalSetupCacheDatasource,
) : LocalSetupDatasource {
    override suspend fun getUserByEmail(
        email: String,
        userType: UserType,
    ): DtoDBUser? = coreDB.getUserByEmail(email, userType)

    override suspend fun createPlaylistFromSpotifyPlaylist(
        user: DtoDBUser,
        spotifySongTitle: List<String>,
    ): DtoFullPlaylist {
        val cacheResult = cache.cacheSongByTitle(spotifySongTitle)
        val notFoundTitle = spotifySongTitle.filter { title ->
            cacheResult.none { it.title.contains(title, ignoreCase = true) }
        }.ifEmpty {
            val playlist = coreDB.createPlaylist(user.id, cacheResult.map { it.id })
            return DtoFullPlaylist(playlist, cacheResult)
        }
        return coroutineScope {
            val foundSongIdList = cacheResult
                .filter { notFoundTitle.any { title -> it.title.contains(title, ignoreCase = true) } }
                .map { it.id }

            val dbSongs = notFoundTitle.map { title ->
                async {
                    kyokuDbQuery {
                        DaoSong.find {
                            EntitySong.title like "$title%" and
                                    (not(EntitySong.title like "%Remix%")) and
                                    (not(EntitySong.title like "%Mashup%")) and
                                    (not(EntitySong.title like "%LoFi%")) and
                                    (not(EntitySong.title like "%New Years%")) and
                                    (not(EntitySong.title like "%Slowed%")) and
                                    (EntitySong.id notInList foundSongIdList)
                        }.groupBy { it.title }.mapNotNull { it.value.firstOrNull() }
                    }
                }
            }.awaitAll().flatten()

            var idList = (dbSongs.map { it.id.value } + cacheResult.map { it.id }).distinctBy { it }

            val songs = if (idList.isNotEmpty()) {
                val albumDef = async { coreDB.getAlbumOnSongId(idList) }
                val artistDef = async { coreDB.getArtistOnSongId(idList) }
                val infoDef = async { coreDB.getInfoOnSongId(idList) }
                val genreDef = async { coreDB.getGenreOnSongId(idList) }

                val artist = artistDef.await()
                val album = albumDef.await()
                val info = infoDef.await()
                val genre = genreDef.await()

                val dbResult = dbSongs.map { song ->
                    song.toSongDto(
                        artist = artist.firstOrNull { it.first == song.id.value }?.second ?: emptyList(),
                        album = album.firstOrNull { it.first == song.id.value }?.second?.copy(poster = song.poster),
                        info = info.firstOrNull { it.first == song.id.value }?.second ?: DtoSongInfo(),
                        genre = genre.firstOrNull { it.first == song.id.value }?.second,
                    )
                }

                dbResult + cacheResult
            } else cacheResult

            cache.setSongById(songs)
            cache.setSongIdByTitle(songs)

            val playlist = coreDB.createPlaylist(user.id, songs.map { it.id })

            DtoFullPlaylist(
                playlist = playlist,
                listOfSong = songs
            )
        }
    }

    override suspend fun updateBDate(
        user: DtoDBUser,
        bDate: String,
    ): Boolean {
        val date = try {
            SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()) // dd-MM-yyyy patter gets converted to yyyy-MM-dd
                .parse(bDate)
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
        } catch (_: Exception) {
            return false
        }

        return userDbQuery {
            DaoUser.find {
                EntityUser.id eq user.id
            }.firstOrNull()?.let {
                it.bDate = date
                true
            }
        } == true
    }

    override suspend fun getPagingGenre(
        page: Int,
        size: Int,
        query: String,
    ): List<DtoGenre> {
        if (query.isBlank()) {
            val list = calculateIdList<Int>(page, size)

            val cache = cache.cacheGenreById(list)
            if (cache.size >= size) return cache

            val limit = size - cache.size
            val idList = list.filter { id -> cache.map { it.id }.none { it == id } }

            val dbGenre = kyokuDbQuery {
                DaoGenre.find {
                    EntityGenre.id inList idList
                }.limit(limit).map { it.toGenreDto() }
            }.also { this.cache.setGenreById(it) }

            return (cache + dbGenre).distinctBy { it.id }
        }


        val cache = cache.cacheGenreByName(query, size)
        if (cache.size >= size) return cache

        val limit = size - cache.size

        val dbGenre = kyokuDbQuery {
            DaoGenre.find {
                EntityGenre.genre like "$query%" and
                        (EntityGenre.genre notInList cache.map { it.name })
            }.limit(limit).map { it.toGenreDto() }
        }.also { this.cache.setGenreIdByName(it.associate { it.name to it.id }) }

        return cache + dbGenre
    }

    override suspend fun upsertGenre(
        user: DtoDBUser,
        data: DtoUpsert<GenreId>,
    ): List<DtoGenre> = coroutineScope {
        val cache = cache.cacheGenreById(data.idList)
        val notFound = data.idList.filter { id -> cache.none { it.id == id } }

        val dbGenre = async {
            if (notFound.isEmpty()) emptyList()
            else kyokuDbQuery {
                DaoGenre.find {
                    EntityGenre.id inList notFound
                }.map { it.toGenreDto() }
            }
        }

        val op = async {
            when (data.operation) {
                DtoUpsertOperation.INSERT -> {
                    val insertRelation = async {
                        userDbQuery {
                            RelationEntityUserGenre.batchInsert(
                                data = data.idList,
                                ignore = true,
                                shouldReturnGeneratedValues = false
                            ) {
                                this[RelationEntityUserGenre.genreId] = it
                                this[RelationEntityUserGenre.userId] = user.id
                            }
                        }
                    }

                    val updatePopularity = async {
                        kyokuDbQuery {
                            DaoGenre.find {
                                EntityGenre.id inList data.idList
                            }.map {
                                it.popularity += 1
                                it.toGenreDto()
                            }
                        }.also { list ->
                            this@ExposedSetupDatasource.cache.setGenreById(list)
                            this@ExposedSetupDatasource.cache.setPrevGenreByUserId(
                                userId = user.id,
                                data = list.map { it.toDtoPrevGenre() }
                            )
                        }
                    }

                    listOf(
                        insertRelation,
                        updatePopularity
                    ).awaitAll()
                }

                DtoUpsertOperation.UPDATE -> TODO()
                DtoUpsertOperation.DELETE -> TODO()
            }
        }

        op.await()
        val result = cache + dbGenre.await()

        result
    }

    override suspend fun getPagingArtist(
        page: Int,
        size: Int,
        query: String,
        userId: Long,
    ): List<DtoPrevArtist> = coroutineScope {
        //  get user selected genre
        val genre = cache.cachePrevGenreByUserId(userId).ifEmpty {
            val genreIds = userDbQuery {
                RelationEntityUserGenre
                    .select(RelationEntityUserGenre.genreId)
                    .where {
                        RelationEntityUserGenre.userId eq userId
                    }.map {
                        it[RelationEntityUserGenre.genreId]
                    }
            }

            val genre = cache.cacheGenreById(genreIds).map { it.toDtoPrevGenre() }
            if (genreIds.size == genre.size) return@ifEmpty genre

            val notFoundGenre = genreIds.filter { id -> genre.map { dto -> dto.id }.none { id == it } }
            genre + kyokuDbQuery {
                DaoGenre.find {
                    EntityGenre.id inList notFoundGenre
                }.map { it.toGenreDto().toDtoPrevGenre() }
            }.also { cache.setPrevGenreByUserId(userId, it) }
        }.ifEmpty { return@coroutineScope emptyList() }

        query.ifBlank {
            //  get artist according to genre and popularity
            val limit = max(size / genre.size, 1) //  calculate number of artist for each genre

            val genreArtistIds = genre.map { dto ->
                async {
                    shardGenreArtistDbQuery { // todo add try cache if error
                        val table = ShardRelationEntityGenreTypeArtist(dto.name)

                        if (table.exists()) table
                            .select(
                                table.artistId,
                                table.popularity
                            )
                            .where {
                                table.genreId eq dto.id
                            }.orderBy(table.popularity to SortOrder.DESC)
                            .limit(limit)
                            .offset(page.offset(limit))
                            .map {
                                it[table.artistId]
                            }
                        else emptyList()
                    }
                }
            }.awaitAll().flatten()

            val genreArtist = cache.cacheArtistById(genreArtistIds)
                .map { it.toDtoPrevArtist() }.ifEmpty {
                    cache.cachePrevArtistById(genreArtistIds).ifEmpty {
                        kyokuDbQuery {
                            DaoArtist.find {
                                EntityArtist.id inList genreArtistIds
                            }.map { it.toDtoPrevArtist() }
                        }.also { cache.setPrevArtistById(it) }
                    }
                }

            if (genreArtist.size >= size) return@coroutineScope genreArtist

            val newLimit = size - genreArtist.size
            val dbArtist = kyokuDbQuery { // add artist outside of picked genre
                EntityArtist
                    .join(
                        otherTable = RelationEntityArtistGenre,
                        joinType = JoinType.LEFT,
                        onColumn = EntityArtist.id,
                        otherColumn = RelationEntityArtistGenre.artistId,
                        additionalConstraint = {
                            RelationEntityArtistGenre.artistId eq EntityArtist.id as Column<*>
                        }
                    )
                    .select(
                        EntityArtist.id,
                        EntityArtist.name,
                        EntityArtist.coverImage,
                        EntityArtist.popularity
                    )
                    .where {
                        RelationEntityArtistGenre.genreId notInList genre.map { it.id } or
                                (RelationEntityArtistGenre.genreId.isNull())
                    }.orderBy(EntityArtist.popularity to SortOrder.DESC)
                    .limit(newLimit)
                    .offset(page.offset(newLimit))
                    .map {
                        DtoPrevArtist(
                            id = it[EntityArtist.id].value,
                            name = it[EntityArtist.name],
                            rawCover = it[EntityArtist.coverImage]
                        )
                    }
            }.also { cache.setPrevArtistById(it) }

            return@coroutineScope genreArtist + dbArtist
        }

        val cache = cache.cachePrevArtistByName(query, size)
        if (cache.size >= size) return@coroutineScope cache

        val limit = size - cache.size
        val dbArtist = kyokuDbQuery {
            DaoArtist.find {
                EntityArtist.name like "$query%" and
                        (EntityArtist.name notInList cache.map { it.name })
            }.orderBy(EntityArtist.popularity to SortOrder.DESC)
                .limit(limit)
                .offset(page.offset(limit))
                .map { it.toDtoPrevArtist() }
        }.also { this@ExposedSetupDatasource.cache.setPrevArtistIdByName(it.associate { it.name to it.id }) }

        cache + dbArtist
    }

    override suspend fun upsertArtist(
        user: DtoDBUser,
        data: DtoUpsert<ArtistId>,
    ): List<DtoArtist> = coroutineScope {
        val cache = cache.cacheArtistById(data.idList)
        val notFound = data.idList.filter { id -> cache.none { it.id == id } }

        val dbArtist = async {
            if (notFound.isEmpty()) emptyList()
            else {
                kyokuDbQuery {
                    DaoArtist.find {
                        EntityArtist.id inList notFound
                    }.map { it.toDbArtistDto() }
                }.let {
                    coreDB.getArtistFromDbArtist(it)
                }
            }
        }

        val op = async {
            when (data.operation) {
                DtoUpsertOperation.INSERT -> {
                    val insertRelation = async {
                        userDbQuery {
                            RelationEntityUserArtist.batchInsert(
                                data = data.idList.map { user.id to it },
                                body = { (userId, artistId) ->
                                    this[RelationEntityUserArtist.userId] = userId
                                    this[RelationEntityUserArtist.artistId] = artistId
                                },
                                ignore = true,
                                shouldReturnGeneratedValues = false
                            )
                        }
                    }

                    val updatePopularity = async {
                        kyokuDbQuery {
                            DaoArtist.find {
                                EntityArtist.id inList data.idList
                            }.map {
                                it.popularity += 1
                            }
                        }
                    }

                    listOf(
                        insertRelation,
                        updatePopularity
                    ).awaitAll()
                }

                DtoUpsertOperation.UPDATE -> TODO()
                DtoUpsertOperation.DELETE -> TODO()
            }
        }

        op.await()
        val result = cache + dbArtist.await()

        result
    }

    @Suppress("UNCHECKED_CAST") // as ids start from 1 we can calculate ids like this
    private fun <T> calculateIdList(page: Int, size: Int): List<T> = (if (page == 1) (page..size).toList() else
        (size * (page - 1) + (page - (page - 1))..size * page).toList()) as List<T>

    @Suppress("KotlinConstantConditions")
    private fun Int.offset(limit: Int) = if (this == 1) 0L else (this * limit).toLong()
}
