package com.poulastaa.core.database.repository.suggestion

import com.poulastaa.core.database.SQLDbManager.kyokuDbQuery
import com.poulastaa.core.database.SQLDbManager.shardGenreArtistDbQuery
import com.poulastaa.core.database.SQLDbManager.shardPopularDbQuery
import com.poulastaa.core.database.dao.DaoArtist
import com.poulastaa.core.database.dao.DaoSong
import com.poulastaa.core.database.entity.app.EntityArtist
import com.poulastaa.core.database.entity.app.EntitySong
import com.poulastaa.core.database.entity.shard.suggestion.ShardEntityArtistPopularSong
import com.poulastaa.core.database.entity.shard.suggestion.ShardEntityCountryPopularSong
import com.poulastaa.core.database.entity.shard.suggestion.ShardEntityYearPopularSong
import com.poulastaa.core.database.entity.user.RelationEntityUserArtist
import com.poulastaa.core.database.mapper.toDtoPrevArtist
import com.poulastaa.core.database.mapper.toDtoPrevSong
import com.poulastaa.core.domain.model.*
import com.poulastaa.core.domain.repository.*
import com.poulastaa.core.domain.repository.suggestion.LocalSuggestionCacheDatasource
import com.poulastaa.core.domain.repository.suggestion.LocalSuggestionDatasource
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.and
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
    ): List<DtoPrevAlbum> {
        TODO("Not yet implemented")
    }

    override suspend fun getSuggestedArtistSong(
        userId: Long,
        oldList: List<ArtistId>,
    ): List<DtoSuggestedArtistSong> {
        TODO("Not yet implemented")
    }

    override suspend fun getSavedPlaylist(userId: Long): List<DtoFullPlaylist> {
        TODO("Not yet implemented")
    }

    override suspend fun getSavedAlbum(userId: Long): List<DtoFullAlbum> {
        TODO("Not yet implemented")
    }

    override suspend fun getSavedArtist(userId: Long): List<DtoArtist> {
        TODO("Not yet implemented")
    }

    private suspend fun getSavedArtistIdOnUserId(userId: Long): List<ArtistId> = kyokuDbQuery {
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