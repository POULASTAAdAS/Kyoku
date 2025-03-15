package com.poulastaa.core.database.repository.view

import com.poulastaa.core.database.SQLDbManager.kyokuDbQuery
import com.poulastaa.core.database.SQLDbManager.shardPopularDbQuery
import com.poulastaa.core.database.SQLDbManager.userDbQuery
import com.poulastaa.core.database.dao.DaoAlbum
import com.poulastaa.core.database.dao.DaoArtist
import com.poulastaa.core.database.dao.DaoPlaylist
import com.poulastaa.core.database.entity.app.*
import com.poulastaa.core.database.entity.shard.suggestion.ShardEntityArtistPopularSong
import com.poulastaa.core.database.entity.shard.suggestion.ShardEntityYearPopularSong
import com.poulastaa.core.database.entity.user.RelationEntityUserArtist
import com.poulastaa.core.database.entity.user.RelationEntityUserFavouriteSong
import com.poulastaa.core.database.mapper.*
import com.poulastaa.core.domain.model.*
import com.poulastaa.core.domain.repository.*
import com.poulastaa.core.domain.repository.view.LocalViewCacheDatasource
import com.poulastaa.core.domain.repository.view.LocalViewDatasource
import kotlinx.coroutines.*
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.and
import java.time.LocalDate
import kotlin.random.Random

internal class ExposedLocalViewDatasource(
    private val core: LocalCoreDatasource,
    private val cache: LocalViewCacheDatasource,
) : LocalViewDatasource {
    override suspend fun getUserByEmail(
        email: String,
        userType: UserType,
    ): DtoDBUser? = core.getUserByEmail(email, userType)

    override suspend fun getArtist(artistId: ArtistId): DtoPrevArtist? =
        cache.cacheArtistById(artistId) ?: kyokuDbQuery {
            DaoArtist.find {
                EntityArtist.id eq artistId
            }.map { it.toDtoPrevArtist() }
        }.first().also {
            cache.setArtistById(it)
        }

    override suspend fun getArtistMostPopularSongs(artistId: ArtistId): List<DtoDetailedPrevSong> {
        val songIdList = shardPopularDbQuery {
            ShardEntityArtistPopularSong.select(ShardEntityArtistPopularSong.id).where {
                ShardEntityArtistPopularSong.artistId eq artistId
            }.map { it[ShardEntityArtistPopularSong.id].value as SongId }
        }

        val songs = songIdList.let { cache.cacheDetailedPrevSongById(it) }.ifEmpty {
            core.getDetailedPrevSongOnId(songIdList)
        }.also { if (it.size == songIdList.size) return it }

        val notFound = songIdList.filterNot { songs.map { it.id }.contains(it) }
        return songs + core.getDetailedPrevSongOnId(notFound).also { cache.setDetailedPrevSongById(it) }
    }

    override suspend fun getPrevFullPlaylist(playlistId: PlaylistId): DtoViewOtherPayload? {
        val playlist = cache.cachePlaylistOnId(playlistId) ?: kyokuDbQuery {
            DaoPlaylist.find {
                EntityPlaylist.id eq playlistId
            }.firstOrNull()
                ?.toDtoPlaylist()
                ?.also { cache.setPlaylistOnId(it) }
        } ?: return null

        val cache = cache.cachePrevDetailedSongByPlaylistId(playlistId)

        return if (cache == null) {
            val songs = kyokuDbQuery {
                RelationSongPlaylist.select(RelationSongPlaylist.songId).where {
                    RelationSongPlaylist.playlistId eq playlistId
                }.map { it[RelationSongPlaylist.songId] as SongId }
            }.let { core.getDetailedPrevSongOnId(it) }
                .also {
                    this.cache.setDetailedPrevSongById(it)
                    this.cache.setSongIdByPlaylistId(playlistId, it.map { it.id })
                }

            DtoViewOtherPayload(
                heading = playlist.toDtoHeading(),
                songs = songs
            )
        } else {
            val dbSongs = core.getDetailedPrevSongOnId(cache.second)
                .also { this.cache.setDetailedPrevSongById(it) }

            DtoViewOtherPayload(
                heading = playlist.toDtoHeading(),
                songs = dbSongs + cache.first
            )
        }
    }

    override suspend fun getPrevFullAlbum(albumId: AlbumId): DtoViewOtherPayload? {
        val album = cache.cacheAlbumById(albumId) ?: kyokuDbQuery {
            DaoAlbum.find {
                EntityAlbum.id eq albumId
            }.firstOrNull()?.toDtoAlbum()?.also { cache.setAlbumById(it) }
        } ?: return null

        val cache = cache.cachePrevDetailedSongByAlbumId(albumId)

        return if (cache == null) {
            val songs = kyokuDbQuery {
                RelationEntitySongAlbum.select(RelationEntitySongAlbum.songId).where {
                    RelationEntitySongAlbum.albumId eq albumId
                }.map { it[RelationEntitySongAlbum.songId] as AlbumId }
            }.let { core.getDetailedPrevSongOnId(it) }
                .also {
                    this.cache.setDetailedPrevSongById(it)
                    this.cache.setSongIdByAlbumId(albumId, it.map { it.id })
                }

            DtoViewOtherPayload(
                heading = album.toDtoHeading(),
                songs = songs
            )
        } else {
            val dbSongs = core.getDetailedPrevSongOnId(cache.second)
                .also { this.cache.setDetailedPrevSongById(it) }

            DtoViewOtherPayload(
                heading = album.toDtoHeading(),
                songs = dbSongs + cache.first
            )
        }
    }

    override suspend fun getPrevFev(userId: Long): DtoViewOtherPayload? {
        val cache = cache.cacheUserFevPrevSong(userId)

        return if (cache == null) {
            val songs = kyokuDbQuery {
                RelationEntityUserFavouriteSong.select(RelationEntityUserFavouriteSong.songId).where {
                    RelationEntityUserFavouriteSong.userId eq userId
                }.map { it[RelationEntityUserFavouriteSong.songId] as SongId }
            }.let { core.getDetailedPrevSongOnId(it) }
                .also {
                    this.cache.setDetailedPrevSongById(it)
                    this.cache.setUserFevPrevSong(userId, it.map { it.id })
                }

            DtoViewOtherPayload(
                heading = DtoHeading(
                    type = DtoViewType.FAVOURITE,
                    name = DtoViewType.FAVOURITE.name
                ),
                songs = songs
            )
        } else {
            val dbSongs = core.getDetailedPrevSongOnId(cache.second)
                .also { this.cache.setDetailedPrevSongById(it) }

            DtoViewOtherPayload(
                heading = DtoHeading(
                    type = DtoViewType.FAVOURITE,
                    name = DtoViewType.FAVOURITE.name
                ),
                songs = dbSongs + cache.first
            )
        }
    }

    override suspend fun getPopularSongMix(
        userId: Long,
        songIds: List<SongId>,
    ): DtoViewOtherPayload? = coroutineScope {
        val reqSongs = async {
            val songs = cache.cacheSongById(songIds).map { it.toDtoDetailedPrevSong() }.ifEmpty {
                cache.cacheDetailedPrevSongById(songIds)
            }.ifEmpty {
                core.getDetailedPrevSongOnId(songIds)
                    .also { cache.setDetailedPrevSongById(it) }
            }

            val notFoundSongIds = songIds.filterNot { songs.map { it.id }.contains(it) }
            val dbSongs = core.getDetailedPrevSongOnId(notFoundSongIds)
                .also { cache.setDetailedPrevSongById(it) }

            dbSongs + songs
        }
        val mostPopularSongs = async {
            val songIdList = kyokuDbQuery {
                EntitySongInfo.select(EntitySongInfo.songId)
                    .orderBy(EntitySongInfo.popularity to SortOrder.DESC)
                    .limit(30)
                    .map { it[EntitySongInfo.songId].value as SongId }
            }.shuffled(Random).take(20)

            getDetailedPrevSongs(songIds, songIdList)
        }
        val fevArtistSongs = getSavedArtistBestSongs(
            userId = userId,
            songIds = songIds,
            takeLimit = 30,
            isArtistLimit = true,
            isTotalLimit = true
        )

        DtoViewOtherPayload(
            heading = DtoHeading(
                type = DtoViewType.POPULAR_SONG_MIX,
                name = DtoViewType.POPULAR_SONG_MIX.name
            ),
            songs = listOf(
                reqSongs,
                mostPopularSongs,
                fevArtistSongs
            ).awaitAll().flatten().distinctBy { it.id }.shuffled(Random)
        )
    }

    override suspend fun getPopularYearMix(
        userId: Long,
        birthYear: Int,
        countryId: CountryId,
        songIds: List<SongId>,
    ): DtoViewOtherPayload? {
        val thisYear = LocalDate.now().year
        val startYear = minOf(thisYear, birthYear + 6)
        val endYear = minOf(thisYear, startYear + 10)

        val songIdList = shardPopularDbQuery {
            ShardEntityYearPopularSong.select(ShardEntityYearPopularSong.id).where {
                ShardEntityYearPopularSong.year greaterEq startYear and
                        (ShardEntityYearPopularSong.year lessEq endYear)
            }.orderBy(org.jetbrains.exposed.sql.Random())
                .limit(Random.nextInt(50, 60)).map {
                    it[ShardEntityYearPopularSong.id].value as SongId
                }
        }

        return DtoViewOtherPayload(
            heading = DtoHeading(
                type = DtoViewType.POPULAR_YEAR_MIX,
                name = DtoViewType.POPULAR_YEAR_MIX.name
            ),
            songs = getDetailedPrevSongs(songIds, songIdList)
        )
    }

    override suspend fun getPopularArtistSongMix(
        userId: Long,
        songIds: List<SongId>,
    ): DtoViewOtherPayload? = coroutineScope {
        val songs = getSavedArtistBestSongs(
            userId = userId,
            songIds = songIds,
            takeLimit = 50,
            isArtistLimit = false,
            isTotalLimit = false
        )

        DtoViewOtherPayload(
            heading = DtoHeading(
                type = DtoViewType.POPULAR_YEAR_MIX,
                name = DtoViewType.POPULAR_YEAR_MIX.name
            ),
            songs = songs.await()
        )
    }

    override suspend fun getPopularDayTimeMix(
        userId: Long,
        songIds: List<SongId>,
    ): DtoViewOtherPayload? {
        TODO("Not yet implemented")
    }

    private suspend fun getDetailedPrevSongs(
        songIds: List<SongId>,
        songIdList: List<SongId>,
    ): List<DtoDetailedPrevSong> {
        val cache = cache.cacheDetailedPrevSongById(songIds)

        val ids = cache.map { it.id }
        val notFoundIds = songIds.filterNot { ids.contains(it) }
        val dbSongs = core.getDetailedPrevSongOnId(notFoundIds + songIdList)
            .also { this@ExposedLocalViewDatasource.cache.setDetailedPrevSongById(it) }

        return dbSongs + cache
    }

    private fun CoroutineScope.getSavedArtistBestSongs(
        userId: Long,
        songIds: List<SongId>,
        takeLimit: Int,
        isArtistLimit: Boolean,
        isTotalLimit: Boolean,
    ): Deferred<List<DtoDetailedPrevSong>> = async {
        val artistIdList = userDbQuery {
            RelationEntityUserArtist.select(RelationEntityUserArtist.artistId).where {
                RelationEntityUserArtist.userId eq userId
            }.shuffled(Random).map { it[RelationEntityUserArtist.artistId] as ArtistId }
        }.let { if (isArtistLimit) it.take(5) else it }

        val songIdList = shardPopularDbQuery {
            ShardEntityArtistPopularSong.select(ShardEntityArtistPopularSong.id).where {
                ShardEntityArtistPopularSong.artistId inList artistIdList
            }.orderBy(org.jetbrains.exposed.sql.Random()).let {
                if (isTotalLimit) it.limit(50) else it
            }.map { it[ShardEntityArtistPopularSong.id].value as SongId }
        }.take(takeLimit)

        getDetailedPrevSongs(songIds, songIdList)
    }
}