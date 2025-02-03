package com.poulastaa.core.database.repository.suggestion

import com.poulastaa.core.database.SQLDbManager.kyokuDbQuery
import com.poulastaa.core.database.SQLDbManager.shardPopularDbQuery
import com.poulastaa.core.database.dao.DaoSong
import com.poulastaa.core.database.entity.app.EntitySong
import com.poulastaa.core.database.entity.shard.suggestion.ShardEntityCountryPopularSong
import com.poulastaa.core.database.mapper.toDtoPrevSong
import com.poulastaa.core.domain.model.*
import com.poulastaa.core.domain.repository.*
import com.poulastaa.core.domain.repository.suggestion.LocalSuggestionCacheDatasource
import com.poulastaa.core.domain.repository.suggestion.LocalSuggestionDatasource
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
                it[ShardEntityCountryPopularSong.id].value
            }
        }

        val songIdList = list.filterNot { a -> oldList.any { it == a } }.let {
            if (it.size < 4) it + oldList.shuffled(Random).take(4 - it.size)
            else it
        }

        if (songIdList.isEmpty()) return emptyList()
        return cache.cachePrevSongById(songIdList).ifEmpty {
            kyokuDbQuery {
                DaoSong.find {
                    EntitySong.id inList songIdList
                }.map { it.toDtoPrevSong() }
            }.also { cache.setPrevSongById(it) }
        }
    }


    override suspend fun getPrevPopularArtistMix(
        userId: Long,
        countryId: CountryId,
        oldList: List<SongId>,
    ): List<DtoPrevSong> {
        TODO("Not yet implemented")
    }

    override suspend fun getPrevPopularYearSongs(
        userId: Long,
        countryId: CountryId,
        oldList: List<SongId>,
    ): List<DtoPrevSong> {
        TODO("Not yet implemented")
    }

    override suspend fun getSuggestedArtist(
        userId: Long,
        oldList: List<ArtistId>,
    ): List<DtoPrevArtist> {
        TODO("Not yet implemented")
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
}