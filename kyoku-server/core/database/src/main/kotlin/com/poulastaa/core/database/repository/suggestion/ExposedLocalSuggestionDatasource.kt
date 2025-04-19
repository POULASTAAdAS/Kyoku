package com.poulastaa.core.database.repository.suggestion

import com.poulastaa.core.database.SQLDbManager.kyokuDbQuery
import com.poulastaa.core.database.SQLDbManager.shardGenreArtistDbQuery
import com.poulastaa.core.database.SQLDbManager.shardPopularDbQuery
import com.poulastaa.core.database.SQLDbManager.userDbQuery
import com.poulastaa.core.database.dao.DaoAlbum
import com.poulastaa.core.database.dao.DaoArtist
import com.poulastaa.core.database.dao.DaoPlaylist
import com.poulastaa.core.database.dao.DaoSong
import com.poulastaa.core.database.entity.app.*
import com.poulastaa.core.database.entity.shard.suggestion.ShardEntityArtistPopularSong
import com.poulastaa.core.database.entity.shard.suggestion.ShardEntityCountryPopularSong
import com.poulastaa.core.database.entity.shard.suggestion.ShardEntityYearPopularSong
import com.poulastaa.core.database.entity.user.RelationEntityUserAlbum
import com.poulastaa.core.database.entity.user.RelationEntityUserArtist
import com.poulastaa.core.database.entity.user.RelationEntityUserFavouriteSong
import com.poulastaa.core.database.entity.user.RelationEntityUserPlaylist
import com.poulastaa.core.database.mapper.*
import com.poulastaa.core.domain.model.*
import com.poulastaa.core.domain.repository.*
import com.poulastaa.core.domain.repository.suggestion.LocalSuggestionCacheDatasource
import com.poulastaa.core.domain.repository.suggestion.LocalSuggestionDatasource
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.jetbrains.exposed.sql.*
import java.time.LocalDate
import kotlin.random.Random

class ExposedLocalSuggestionDatasource(
    private val core: LocalCoreDatasource,
    private val cache: LocalSuggestionCacheDatasource,
) : LocalSuggestionDatasource {
    override suspend fun getUserByEmail(
        email: String,
        userType: UserType,
    ): DtoDBUser? = core.getUserByEmail(email, userType)

    override suspend fun getPrevPopularCountrySong(
        userId: Long,
        countryId: CountryId,
        oldList: List<SongId>,
    ): List<DtoPrevSong> {
        val list = shardPopularDbQuery {
            ShardEntityCountryPopularSong.select(ShardEntityCountryPopularSong.id).where {
                ShardEntityCountryPopularSong.countryId eq countryId
            }.map {
                it[ShardEntityCountryPopularSong.id].value as SongId
            }
        }

        val songIdList = list.filterNot { a -> oldList.any { it == a } }.let {
            if (it.size < 4) it + oldList.shuffled(Random).take(4 - it.size)
            else it
        }

        if (songIdList.isEmpty()) return emptyList()
        return getPrevSongById(songIdList).shuffled(Random).take(4)
    }

    override suspend fun getPrevPopularArtistMix(
        userId: Long,
        oldList: List<SongId>,
    ): List<DtoPrevSong> {
        val artistIdList = getSavedArtistIdOnUserId(userId)

        val songIdList = shardPopularDbQuery {
            val query = ShardEntityArtistPopularSong.select(ShardEntityArtistPopularSong.id)

            when {
                artistIdList.isEmpty() && oldList.isEmpty() -> query
                artistIdList.isEmpty() -> query.where {
                    ShardEntityArtistPopularSong.artistId notInList oldList
                }

                else -> query.where {
                    ShardEntityArtistPopularSong.artistId inList artistIdList and
                            (ShardEntityArtistPopularSong.id notInList oldList)
                }
            }.orderBy(org.jetbrains.exposed.sql.Random())
                .limit(4).map {
                    it[ShardEntityArtistPopularSong.id].value as SongId
                }
        }

        return getPrevSongById(songIdList)
    }

    override suspend fun getPrevPopularYearSongs(
        userId: Long,
        birthYear: Int,
        oldList: List<SongId>,
    ): List<DtoPrevSong> {
        val thisYear = LocalDate.now().year
        val startYear = minOf(thisYear, birthYear + 6)
        val endYear = minOf(thisYear, startYear + 10)

        val songIdList = shardPopularDbQuery {
            ShardEntityYearPopularSong.select(ShardEntityYearPopularSong.id).where {
                ShardEntityYearPopularSong.year greaterEq startYear and
                        (ShardEntityYearPopularSong.year lessEq endYear)
            }.orderBy(org.jetbrains.exposed.sql.Random())
                .limit(4).map {
                    it[ShardEntityYearPopularSong.id].value as SongId
                }
        }

        return getPrevSongById(songIdList)
    }

    override suspend fun getSuggestedArtist(
        userId: Long,
        countryId: CountryId,
        oldList: List<ArtistId>,
    ): List<DtoPrevArtist> {
        val artistIdList = getSavedArtistIdOnUserId(userId)
        if (oldList.isEmpty() && artistIdList.isEmpty()) return emptyList()

        return shardGenreArtistDbQuery {
            DaoArtist.find {
                EntityArtist.id notInList oldList + artistIdList
            }.orderBy(EntityArtist.popularity to SortOrder.DESC)
                .limit(20)
                .map { it.toDtoPrevArtist() }
                .shuffled(Random)
                .take(10)
        }
    }

    override suspend fun getSuggestedAlbum(
        userId: Long,
        oldList: List<AlbumId>,
    ): List<DtoAlbum> {
        val savedAlbumIdList = userDbQuery {
            RelationEntityUserAlbum.select(RelationEntityUserAlbum.albumId).where {
                RelationEntityUserAlbum.userId eq userId
            }.map {
                it[RelationEntityUserAlbum.albumId] as AlbumId
            }
        }

        return kyokuDbQuery {
            EntityAlbum
                .join(
                    otherTable = RelationEntitySongAlbum,
                    joinType = JoinType.INNER,
                    onColumn = EntityAlbum.id,
                    otherColumn = RelationEntitySongAlbum.albumId,
                    additionalConstraint = {
                        RelationEntitySongAlbum.albumId eq EntityAlbum.id
                    }
                )
                .join(
                    otherTable = EntitySong,
                    joinType = JoinType.INNER,
                    onColumn = EntitySong.id,
                    otherColumn = RelationEntitySongAlbum.songId,
                    additionalConstraint = {
                        EntitySong.id eq RelationEntitySongAlbum.songId
                    }
                ).select(
                    EntityAlbum.id,
                    EntityAlbum.name,
                    EntitySong.poster
                ).run {
                    if (savedAlbumIdList.isEmpty() && oldList.isEmpty()) this
                    else where {
                        EntityAlbum.id notInList oldList + savedAlbumIdList
                    }
                }
                .orderBy(EntityAlbum.popularity to SortOrder.DESC)
                .distinctBy { it[EntityAlbum.id] }
                .take(20)
                .map {
                    DtoAlbum(
                        id = it[EntityAlbum.id].value,
                        name = it[EntityAlbum.name],
                        rawPoster = it[EntitySong.poster],
                        popularity = -1 // no need to pass popularity
                    )
                }.shuffled(Random)
                .take(10)
        }
    }

    override suspend fun getSuggestedArtistSong(
        userId: Long,
        oldList: List<ArtistId>,
        suggestedArtistIdList: List<ArtistId>,
    ): List<DtoSuggestedArtistSong> {
        val savedArtistIdList = getSavedArtistIdOnUserId(userId)
        val artist = kyokuDbQuery {
            DaoArtist.find {
                EntityArtist.id notInList oldList + suggestedArtistIdList + savedArtistIdList
            }.orderBy(EntityArtist.popularity to SortOrder.DESC)
                .limit(20)
                .shuffled(Random)
                .take(10)
                .map { it.toDtoPrevArtist() }
        }

        val rank = kyokuDbQuery {
            RowNumber()
                .over()
                .partitionBy(RelationEntitySongArtist.artistId)
                .orderBy(EntitySongInfo.popularity to SortOrder.DESC)
                .alias("`rank`")
        }
        val rankedQuery = kyokuDbQuery {
            EntitySong
                .join(
                    otherTable = EntitySongInfo,
                    joinType = JoinType.INNER,
                    onColumn = EntitySong.id,
                    otherColumn = EntitySongInfo.songId,
                    additionalConstraint = {
                        EntitySong.id eq EntitySongInfo.songId
                    }
                ).join(
                    otherTable = RelationEntitySongArtist,
                    joinType = JoinType.INNER,
                    onColumn = RelationEntitySongArtist.songId,
                    otherColumn = EntitySong.id,
                    additionalConstraint = {
                        EntitySong.id eq RelationEntitySongArtist.songId
                    }
                ).select(
                    EntitySong.id,
                    EntitySong.title,
                    EntitySong.poster,
                    EntitySongInfo.popularity,
                    RelationEntitySongArtist.artistId,
                    rank
                ).where {
                    RelationEntitySongArtist.artistId inList artist.map { it.id }
                }.alias("RankedSongs")
        }

        return kyokuDbQuery {
            rankedQuery.select(
                rankedQuery[EntitySong.id],
                rankedQuery[EntitySong.title],
                rankedQuery[EntitySong.poster],
                rankedQuery[RelationEntitySongArtist.artistId],
                rankedQuery[EntitySongInfo.popularity]
            ).where {
                rankedQuery[rank].lessEq(longLiteral(10))
            }.orderBy(rankedQuery[EntitySongInfo.popularity] to SortOrder.DESC).map {
                it[rankedQuery[RelationEntitySongArtist.artistId]] to DtoPrevSong(
                    id = it[rankedQuery[EntitySong.id]].value,
                    title = it[rankedQuery[EntitySong.title]],
                    rawPoster = it[rankedQuery[EntitySong.poster]]
                )
            }
        }.groupBy { it.first }.map { (artistId, list) ->
            DtoSuggestedArtistSong(
                artist = artist.first { it.id == artistId },
                prevSong = list.map { it.second }
            )
        }
    }

    override suspend fun getSavedPlaylist(userId: Long): List<DtoFullPlaylist> {
        val playlistIdList = userDbQuery {
            RelationEntityUserPlaylist.select(RelationEntityUserPlaylist.playlistId).where {
                RelationEntityUserPlaylist.userId eq userId
            }.map {
                it[RelationEntityUserPlaylist.playlistId] as PlaylistId
            }
        }

        return coroutineScope {
            kyokuDbQuery {
                EntityPlaylist
                    .join(
                        otherTable = RelationEntitySongPlaylist,
                        joinType = JoinType.LEFT,
                        onColumn = EntityPlaylist.id,
                        otherColumn = RelationEntitySongPlaylist.playlistId
                    ).join(
                        otherTable = EntitySong,
                        joinType = JoinType.LEFT,
                        onColumn = EntitySong.id,
                        otherColumn = RelationEntitySongPlaylist.songId
                    ).select(
                        EntityPlaylist.id,
                        EntitySong.id
                    ).where {
                        EntityPlaylist.id inList playlistIdList
                    }.map {
                        Pair<PlaylistId, SongId?>(
                            first = it[EntityPlaylist.id].value,
                            second = it.getOrNull(EntitySong.id)?.value
                        )
                    }.groupBy { it.first }.map {
                        it.key to it.value.mapNotNull { map -> map.second }
                    }.map { (playlistId, songIdList) ->
                        async {
                            val playlist = async {
                                cache.cachePlaylistOnId(playlistId) ?: kyokuDbQuery {
                                    DaoPlaylist.find {
                                        EntityPlaylist.id eq playlistId
                                    }.first().toDtoPlaylist().also { cache.setPlaylistOnId(it) }
                                }
                            }

                            val songs = async {
                                core.getSongOnId(songIdList)
                            }

                            DtoFullPlaylist(
                                playlist = playlist.await(),
                                listOfSong = songs.await()
                            )
                        }
                    }.awaitAll()
            }
        }
    }

    override suspend fun getSavedAlbum(userId: Long): List<DtoFullAlbum> {
        val albumIdList = userDbQuery {
            RelationEntityUserAlbum.select(RelationEntityUserAlbum.albumId)
                .where { RelationEntityUserAlbum.userId eq userId }
                .map { it[RelationEntityUserAlbum.albumId] as AlbumId }
        }

        val cacheAlbums = cache.cacheAlbumById(albumIdList)
        val notFound = albumIdList.filter { id -> cacheAlbums.none { it.id == id } }

        val albums = kyokuDbQuery {
            DaoAlbum.find {
                EntityAlbum.id inList notFound
            }.map { it.toDtoAlbum() }.also { cache.setAlbumById(it) }
        } + cacheAlbums

        return kyokuDbQuery {
            RelationEntitySongAlbum.selectAll()
                .where { RelationEntitySongAlbum.albumId inList albums.map { it.id } }
                .map { it[RelationEntitySongAlbum.albumId] to it[RelationEntitySongAlbum.songId] }
        }.groupBy { it.first }.map { it.key as AlbumId to it.value.map { it.second as SongId } }
            .map { (albumId, songIdList) ->
                DtoFullAlbum(
                    album = albums.first { it.id == albumId },
                    songs = core.getSongOnId(songIdList)
                )
            }
    }

    override suspend fun getSavedArtist(userId: Long): List<DtoArtist> = coroutineScope {
        val artistIdList = userDbQuery {
            RelationEntityUserArtist.select(RelationEntityUserArtist.artistId).where {
                RelationEntityUserArtist.userId eq userId
            }.map { it[RelationEntityUserArtist.artistId] as ArtistId }
        }

        val cacheArtist = cache.cacheArtistById(artistIdList)
        val notFound = artistIdList.filter { id -> cacheArtist.none { it.id == id } }

        kyokuDbQuery {
            DaoArtist.find {
                EntityArtist.id inList notFound
            }.map { it.toDbArtistDto() }
        }.map { artist ->
            val genre = core.getGenreOnArtistId(artist.id)
            val country = core.getCountryOnArtistId(artist.id)

            artist.toArtistDto(
                genre = genre,
                country = country
            )
        }.also { cache.setArtistById(it) } + cacheArtist
    }

    override suspend fun getYourFavouriteSongToAddToPlaylist(
        userId: UserId,
    ): List<DtoDetailedPrevSong> = userDbQuery {
        RelationEntityUserFavouriteSong.select(RelationEntityUserFavouriteSong.songId).where {
            RelationEntityUserFavouriteSong.userId eq userId
        }.map { it[RelationEntityUserFavouriteSong.songId] }
    }.let {
        val cache = cache.cacheDetailedPrevSongById(it)
        val idList = cache.map { item -> item.id }
        val notFound = it.filterNot { item -> item in idList }
        cache + core.getDetailedPrevSongOnId(notFound)
    }

    override suspend fun getSuggestedSongToAddToPlaylist(): List<DtoDetailedPrevSong> = kyokuDbQuery {
        EntitySong.join(
            otherTable = EntitySongInfo,
            joinType = JoinType.INNER,
            onColumn = EntitySong.id,
            otherColumn = EntitySongInfo.songId
        ).select(EntitySong.id, EntitySong.title, EntitySong.poster, EntitySongInfo.popularity)
            .orderBy(EntitySongInfo.popularity to SortOrder.DESC)
            .limit(Random.nextInt(30, 40)).map {
                DtoPrevSong(
                    id = it[EntitySong.id].value,
                    title = it[EntitySong.title],
                    rawPoster = it[EntitySong.poster]
                )
            }
    }.let { list ->
        coroutineScope {
            list.map { song ->
                async {
                    val artist = cache.cacheArtistIdOnSongId(song.id).ifEmpty {
                        kyokuDbQuery {
                            RelationEntitySongArtist.select(RelationEntitySongArtist.artistId).where {
                                RelationEntitySongArtist.songId eq song.id
                            }.map { it[RelationEntitySongArtist.artistId] }
                        }.also { cache.setArtistIdOnSongId(song.id, it) }
                    }.let { list ->
                        core.getArtistOnId(list).joinToString(",") { it.name }
                    }

                    DtoDetailedPrevSong(
                        id = song.id,
                        title = song.title,
                        rawPoster = song.poster,
                        artists = artist
                    )
                }
            }.awaitAll()
        }
    }

    override suspend fun getYouMayAlsoLikeSongToAddToPlaylist(
        countryId: CountryId,
    ): List<DtoDetailedPrevSong> = kyokuDbQuery {
        val aggregatedArtists = GroupConcat(
            expr = EntityArtist.name,
            separator = ", ",
            distinct = true
        ).alias("artists")

        EntitySong.join(
            otherTable = RelationEntitySongArtist,
            joinType = JoinType.LEFT,
            onColumn = EntitySong.id,
            otherColumn = RelationEntitySongArtist.songId,
            additionalConstraint = {
                EntitySong.id eq RelationEntitySongArtist.songId
            }
        ).join(
            otherTable = EntityArtist,
            joinType = JoinType.INNER,
            onColumn = EntityArtist.id,
            otherColumn = RelationEntitySongArtist.artistId,
            additionalConstraint = {
                RelationEntitySongArtist.artistId eq EntityArtist.id
            }
        ).join(
            otherTable = RelationEntityArtistCountry,
            joinType = JoinType.INNER,
            onColumn = EntityArtist.id,
            otherColumn = RelationEntityArtistCountry.artistId,
            additionalConstraint = {
                RelationEntityArtistCountry.artistId eq EntityArtist.id
            }
        ).join(
            otherTable = EntitySongInfo,
            joinType = JoinType.INNER,
            onColumn = EntitySong.id,
            otherColumn = EntitySongInfo.songId,
            additionalConstraint = {
                EntitySongInfo.songId eq EntitySong.id
            }
        ).select(
            EntitySong.id,
            EntitySong.title,
            EntitySong.poster,
            EntitySongInfo.popularity,
            aggregatedArtists
        ).where {
            RelationEntityArtistCountry.countryId neq countryId
        }.groupBy(EntitySong.id, EntitySong.title, EntitySong.poster, EntitySongInfo.popularity)
            .orderBy(EntitySongInfo.popularity to SortOrder.DESC)
            .limit(Random.nextInt(30, 40)).map {
                DtoDetailedPrevSong(
                    id = it[EntitySong.id].value,
                    title = it[EntitySong.title],
                    rawPoster = it[EntitySong.poster],
                    artists = it[aggregatedArtists],
                )
            }
    }

    private suspend fun getSavedArtistIdOnUserId(userId: Long): List<ArtistId> = userDbQuery {
        RelationEntityUserArtist.select(RelationEntityUserArtist.artistId).where {
            RelationEntityUserArtist.userId eq userId
        }.map {
            it[RelationEntityUserArtist.artistId] as ArtistId
        }
    }

    private suspend fun getPrevSongById(songIdList: List<Long>): List<DtoPrevSong> {
        val cache = cache.cachePrevSongById(songIdList)
        val notFound = songIdList.filter { id -> cache.none { it.id == id } }

        val db = kyokuDbQuery {
            DaoSong.find {
                EntitySong.id inList notFound
            }.map { it.toDtoPrevSong() }
        }.also { this.cache.setPrevSongById(it) }

        return cache + db
    }
}