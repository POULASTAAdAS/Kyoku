package com.poulastaa.core.database.repository.setup

import com.poulastaa.core.database.SQLDbManager.kyokuDbQuery
import com.poulastaa.core.database.SQLDbManager.userDbQuery
import com.poulastaa.core.database.dao.DaoGenre
import com.poulastaa.core.database.dao.DaoSong
import com.poulastaa.core.database.dao.DaoUser
import com.poulastaa.core.database.entity.app.EntityGenre
import com.poulastaa.core.database.entity.app.EntitySong
import com.poulastaa.core.database.entity.user.EntityUser
import com.poulastaa.core.database.mapper.toGenreDto
import com.poulastaa.core.database.mapper.toSongDto
import com.poulastaa.core.domain.model.*
import com.poulastaa.core.domain.repository.LocalCoreDatasource
import com.poulastaa.core.domain.repository.setup.LocalSetupCacheDatasource
import com.poulastaa.core.domain.repository.setup.LocalSetupDatasource
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.and
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

    override suspend fun getPagingGenre(genreIds: List<Int>): List<DtoGenre> {
        val cacheGenre = cache.cacheGenre(10, genreIds)
        if (cacheGenre.size == 10) return cacheGenre

        val excludeFullList = genreIds + cacheGenre.map { it.id }
        val limit = 10 - cacheGenre.size
        val dbGenre = kyokuDbQuery {
            DaoGenre.find {
                EntityGenre.id notInList excludeFullList
            }.orderBy(EntityGenre.popularity to SortOrder.DESC)
                .limit(limit).map { it.toGenreDto() }
        }.also { cache.setGenreById(it) }

        return cacheGenre + dbGenre
    }
}