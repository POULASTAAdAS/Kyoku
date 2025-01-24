package com.poulastaa.core.database.repository.setup

import com.poulastaa.core.database.SQLDbManager.kyokuDbQuery
import com.poulastaa.core.database.SQLDbManager.userDbQuery
import com.poulastaa.core.database.dao.DaoArtist
import com.poulastaa.core.database.dao.DaoGenre
import com.poulastaa.core.database.dao.DaoSong
import com.poulastaa.core.database.dao.DaoUser
import com.poulastaa.core.database.entity.app.EntityArtist
import com.poulastaa.core.database.entity.app.EntityGenre
import com.poulastaa.core.database.entity.app.EntitySong
import com.poulastaa.core.database.entity.user.EntityUser
import com.poulastaa.core.database.entity.user.RelationEntityUserGenre
import com.poulastaa.core.database.mapper.toDtoPrevArtist
import com.poulastaa.core.database.mapper.toGenreDto
import com.poulastaa.core.database.mapper.toSongDto
import com.poulastaa.core.domain.model.*
import com.poulastaa.core.domain.repository.GenreId
import com.poulastaa.core.domain.repository.LocalCoreDatasource
import com.poulastaa.core.domain.repository.setup.LocalSetupCacheDatasource
import com.poulastaa.core.domain.repository.setup.LocalSetupDatasource
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insertIgnore
import org.jetbrains.exposed.sql.not
import java.text.SimpleDateFormat
import java.time.ZoneId
import java.util.*

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
    ): DtoPlaylistFull {
        val cacheResult = cache.cacheSongByTitle(spotifySongTitle)
        val notFoundTitle = spotifySongTitle.filter { title ->
            cacheResult.none { it.title.contains(title, ignoreCase = true) }
        }

        if (notFoundTitle.isEmpty()) {
            val playlist = coreDB.createPlaylist(user.id, cacheResult.map { it.id })
            return DtoPlaylistFull(playlist, cacheResult)
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

            DtoPlaylistFull(
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
            val idList = list - cache.map { it.id }

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
        list: List<DtoUpsert<GenreId>>,
    ): List<DtoGenre> = coroutineScope {
        val cache = cache.cacheGenreById(list.map { it.id })
        val notFound = list.map { it.id }.filter { id -> cache.none { it.id == id } }

        val dbGenre = async {
            kyokuDbQuery {
                DaoGenre.find {
                    EntityGenre.id inList notFound
                }.map { it.toGenreDto() }
            }
        }

        val op = list.map { genre ->
            async {
                when (genre.operation) {
                    DtoUpsertOperation.INSERT -> {
                        val insert = async {
                            userDbQuery {
                                RelationEntityUserGenre.insertIgnore {
                                    it[userId] = user.id
                                    it[genreId] = genre.id.toLong() // todo change table to int
                                }
                            } // todo set up cache if needed
                        }

                        val update = async {
                            kyokuDbQuery {
                                DaoGenre.find {
                                    EntityGenre.id eq genre.id
                                }.firstOrNull()?.let {
                                    it.popularity += 1
                                    it.toGenreDto()
                                }
                            }?.also {
                                this@ExposedSetupDatasource.cache.setGenreById(listOf(it))
                            }
                        }

                        listOf(
                            insert,
                            update
                        ).awaitAll()
                    }

                    DtoUpsertOperation.UPDATE -> TODO()
                    DtoUpsertOperation.DELETE -> TODO()
                }
            }
        }

        op.awaitAll()
        val result = cache + dbGenre.await()

        result
    }

    override suspend fun getPagingArtist(
        page: Int,
        size: Int,
        query: String,
    ): List<DtoPrevArtist> {
        if (query.isBlank()) {
            val idList = calculateIdList<Long>(page, size)
            val cache = cache.cachePrevArtistById(idList)

            if (cache.size >= size) return cache

            val limit = size - cache.size
            val list = idList.filterNot { cache.map { it.id }.any { id -> id == it } }

            val dbArtist = kyokuDbQuery {
                DaoArtist.find {
                    EntityArtist.id inList list
                }.limit(limit).map { it.toDtoPrevArtist() }
            }.also { this.cache.setPrevArtistById(it) }

            return (cache + dbArtist).sortedBy { it.id }.distinctBy { it.id }
        }

        val cache = cache.cachePrevArtistByName(query, size)
        if (cache.size >= size) return cache

        val limit = size - cache.size

        val dbArtist = kyokuDbQuery {
            DaoArtist.find {
                EntityArtist.name like "$query%" and
                        (EntityArtist.name notInList cache.map { it.name })
            }.limit(limit).map { it.toDtoPrevArtist() }
        }.also { this.cache.setPrevArtistIdByName(it.associate { it.name to it.id }) }

        return cache + dbArtist
    }

    // as ids start from 1 we can calculate ids like this
    private fun <T> calculateIdList(page: Int, size: Int): List<T> = (if (page == 1) (page..size).toList() else
        (size * (page - 1) + (page - (page - 1))..size * page).toList()) as List<T>
}
