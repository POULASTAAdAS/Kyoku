package com.poulastaa.core.database.repository.suggestion

import com.poulastaa.core.database.SQLDbManager.kyokuDbQuery
import com.poulastaa.core.database.entity.app.EntitySong
import com.poulastaa.core.database.entity.app.RelationEntitySongCountry
import com.poulastaa.core.domain.model.*
import com.poulastaa.core.domain.repository.*
import com.poulastaa.core.domain.repository.suggestion.LocalSuggestionDatasource
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select

class ExposedRedisLocalSuggestionDatasource(
    private val core: LocalCoreDatasource,
) : LocalSuggestionDatasource {
    override suspend fun getUserByEmail(
        email: String,
        userType: UserType,
    ): DtoDBUser? = core.getUserByEmail(email, userType)

    override suspend fun getPrevPopularSongMix(
        userId: Long,
        countryId: CountryId,
        oldList: List<SongId>,
    ): List<DtoPrevSong> = kyokuDbQuery {
        EntitySong
            .join(
                otherColumn = RelationEntitySongCountry.songId,
                otherTable = RelationEntitySongCountry,
                joinType = JoinType.INNER,
                onColumn = EntitySong.id,
                additionalConstraint = {
                    RelationEntitySongCountry.songId eq EntitySong.id as Column<*>
                }
            )
            .slice(
                EntitySong.id,
                EntitySong.title,
                EntitySong.poster
            ).select {
                if (oldList.isNotEmpty()) RelationEntitySongCountry.countryId eq countryId and (EntitySong.id notInList oldList)
                else RelationEntitySongCountry.countryId eq countryId
            }
            .limit(4)
            .map {
                DtoPrevSong(
                    id = it[EntitySong.id].value,
                    title = it[EntitySong.title],
                    rawPoster = it[EntitySong.poster]
                )
            }
    }

    override suspend fun getPrevPopularArtistMix(
        userId: Long,
        countryId: CountryId,
        oldList: List<SongId>,
    ): List<DtoPrevSong> {
        TODO("Not yet implemented")
    }

    override suspend fun getPrevOldGem(
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