package com.poulastaa.core.database.repository.suggestion

import com.poulastaa.core.database.SQLDbManager.kyokuDbQuery
import com.poulastaa.core.database.SQLDbManager.shardGenreArtistDbQuery
import com.poulastaa.core.database.SQLDbManager.shardPopularDbQuery
import com.poulastaa.core.database.SQLDbManager.userDbQuery
import com.poulastaa.core.database.dao.DaoArtist
import com.poulastaa.core.database.dao.DaoPlaylist
import com.poulastaa.core.database.dao.DaoSong
import com.poulastaa.core.database.entity.app.*
import com.poulastaa.core.database.entity.shard.suggestion.ShardEntityArtistPopularSong
import com.poulastaa.core.database.entity.shard.suggestion.ShardEntityCountryPopularSong
import com.poulastaa.core.database.entity.shard.suggestion.ShardEntityYearPopularSong
import com.poulastaa.core.database.entity.user.RelationEntityUserAlbum
import com.poulastaa.core.database.entity.user.RelationEntityUserArtist
import com.poulastaa.core.database.entity.user.RelationEntityUserPlaylist
import com.poulastaa.core.database.mapper.toDtoPlaylist
import com.poulastaa.core.database.mapper.toDtoPrevArtist
import com.poulastaa.core.database.mapper.toDtoPrevSong
import com.poulastaa.core.domain.model.*
import com.poulastaa.core.domain.repository.*
import com.poulastaa.core.domain.repository.suggestion.LocalSuggestionCacheDatasource
import com.poulastaa.core.domain.repository.suggestion.LocalSuggestionDatasource
import kotlinx.coroutines.async
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
        return getPrevSongById(songIdList)
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
        countryId: CountryId,
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

        if (oldList.isEmpty() && savedAlbumIdList.isEmpty()) return emptyList()

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
                )
                .where {
                    EntityAlbum.id notInList oldList + savedAlbumIdList
                }
                .orderBy(EntityAlbum.popularity to SortOrder.DESC)
                .distinct()
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
                RelationSongPlaylist.selectAll().where {
                    RelationSongPlaylist.playlistId inList playlistIdList
                }.map {
                    Pair(
                        first = it[RelationSongPlaylist.playlistId] as PlaylistId,
                        second = it[RelationSongPlaylist.songId] as SongId
                    )
                }
            }.groupBy { it.first as PlaylistId }
                .map { it.key to it.value.map { it.second } }
                .map { (playlistId, songIdList) ->
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
        }
    }

    override suspend fun getSavedAlbum(userId: Long): List<DtoFullAlbum> {
        TODO("Not yet implemented")
    }

    override suspend fun getSavedArtist(userId: Long): List<DtoArtist> {
        TODO("Not yet implemented")
    }

    private suspend fun getSavedArtistIdOnUserId(userId: Long): List<ArtistId> = userDbQuery {
        RelationEntityUserArtist.select(RelationEntityUserArtist.artistId).where {
            RelationEntityUserArtist.userId eq userId
        }.map {
            it[RelationEntityUserArtist.artistId] as ArtistId
        }
    }

    private suspend fun getPrevSongById(songIdList: List<Long>): List<DtoPrevSong> =
        cache.cachePrevSongById(songIdList).ifEmpty {
            kyokuDbQuery {
                DaoSong.find {
                    EntitySong.id inList songIdList
                }.map { it.toDtoPrevSong() }
            }.also { cache.setPrevSongById(it) }
        }
}