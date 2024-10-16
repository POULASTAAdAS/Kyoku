package com.poulastaa.data.repository

import com.poulastaa.data.mappers.constructArtistProfileUrl
import com.poulastaa.data.mappers.constructSongCoverImage
import com.poulastaa.data.mappers.toSongDto
import com.poulastaa.data.model.ArtistDto
import com.poulastaa.data.model.SongDto
import com.poulastaa.data.model.home.PreArtistSongDto
import com.poulastaa.data.model.home.PrevAlbumDto
import com.poulastaa.data.model.home.PrevSongDetailDto
import com.poulastaa.data.model.home.PrevSongDto
import com.poulastaa.domain.model.DayType
import com.poulastaa.domain.model.UserType
import com.poulastaa.domain.repository.DatabaseRepository
import com.poulastaa.domain.repository.HomeRepository
import com.poulastaa.domain.table.AlbumTable
import com.poulastaa.domain.table.ArtistTable
import com.poulastaa.domain.table.SongTable
import com.poulastaa.domain.table.relation.*
import com.poulastaa.plugins.query
import kotlinx.coroutines.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.like

class HomeRepositoryImpl(
    private val database: DatabaseRepository,
) : HomeRepository {
    private fun getPopularSonMixQuery(countryId: Int) = SongTable
        .join(
            otherTable = SongCountryRelationTable,
            joinType = JoinType.INNER,
            additionalConstraint = {
                SongTable.id eq SongCountryRelationTable.songId as Column<*>
            }
        ).select {
            SongCountryRelationTable.countryId eq countryId
        }.orderBy(SongTable.points to SortOrder.DESC)

    private fun getPopularSongFromUserTimeQuery(
        year: Int,
        countryId: Int,
    ) = SongTable.join(
        otherTable = SongCountryRelationTable,
        joinType = JoinType.INNER,
        additionalConstraint = {
            SongTable.id eq SongCountryRelationTable.songId as Column<*>
        }
    ).select {
        SongCountryRelationTable.countryId eq countryId and (SongTable.year eq year)
    }.orderBy(SongTable.points to SortOrder.DESC)

    private fun getFavouriteArtistMixQuery(userId: Long, userType: String) = SongTable
        .join(
            otherTable = SongArtistRelationTable,
            joinType = JoinType.INNER,
            additionalConstraint = {
                SongTable.id eq SongArtistRelationTable.songId as Column<*>
            }
        )
        .join(
            otherTable = ArtistTable,
            joinType = JoinType.INNER,
            additionalConstraint = {
                ArtistTable.id eq SongArtistRelationTable.artistId as Column<*>
            }
        )
        .join(
            otherTable = UserArtistRelationTable,
            joinType = JoinType.INNER,
            additionalConstraint = {
                UserArtistRelationTable.artistId as Column<*> eq ArtistTable.id
            }
        ).select {
            UserArtistRelationTable.userId eq userId and
                    (UserArtistRelationTable.userType eq userType)
        }.orderBy(
            Random() to SortOrder.ASC,
            SongTable.points to SortOrder.DESC
        )

    override suspend fun getPopularSongMixPrev(countryId: Int): List<PrevSongDto> = withContext(Dispatchers.IO) {
        query {
            getPopularSonMixQuery(countryId)
                .limit(4)
                .map {
                    PrevSongDto(
                        songId = it[SongTable.id].value,
                        coverImage = it[SongTable.coverImage].constructSongCoverImage()
                    )
                }
        }
    }

    override suspend fun getPopularSongFromUserTimePrev(
        year: Int,
        countryId: Int,
    ): List<PrevSongDto> = withContext(Dispatchers.IO) {
        (1..4).map { index ->
            async {
                val targetYear = year - 1 + index

                query {
                    getPopularSongFromUserTimeQuery(
                        year = targetYear,
                        countryId = countryId,
                    ).firstOrNull()
                        ?.let {
                            PrevSongDto(
                                songId = it[SongTable.id].value,
                                coverImage = it[SongTable.coverImage].constructSongCoverImage()
                            )
                        }
                }
            }
        }.awaitAll().filterNotNull()
    }

    override suspend fun getFavouriteArtistMixPrev(
        userId: Long,
        userType: UserType,
        countryId: Int,
    ): List<PrevSongDto> = withContext(Dispatchers.IO) {
        query {
            getFavouriteArtistMixQuery(userId, userType.name)
                .limit(4)
                .map {
                    PrevSongDto(
                        songId = it[SongTable.id].value,
                        coverImage = it[SongTable.coverImage].constructSongCoverImage()
                    )
                }
        }
    }

    override suspend fun getDayTypeSongPrev(
        dayType: DayType,
        countryId: Int,
    ): List<PrevSongDto> = withContext(Dispatchers.IO) {
        when (dayType) {
            DayType.MORNING -> emptyList()
            DayType.DAY -> emptyList()
            DayType.NIGHT -> {
                query {
                    SongTable
                        .innerJoin(SongCountryRelationTable)
                        .slice(
                            SongTable.id,
                            SongTable.title,
                            SongTable.coverImage,
                            SongTable.points
                        )
                        .select {
                            SongCountryRelationTable.countryId eq countryId and
                                    (SongTable.title like "%lofi%") or (SongTable.title like "%remix%")
                        }
                        .orderBy(
                            Case().When(SongTable.title like "%lofi%", intLiteral(1))
                                .Else(intLiteral(2)) to SortOrder.ASC,
                            SongTable.points to SortOrder.DESC
                        )
                        .limit(4)
                        .distinct()
                        .toList()
                        .map {
                            PrevSongDto(
                                songId = it[SongTable.id].value,
                                coverImage = it[SongTable.coverImage].constructSongCoverImage()
                            )
                        }
                }
            }
        }
    }

    override suspend fun getPopularAlbumPrev(countryId: Int): List<PrevAlbumDto> = withContext(Dispatchers.IO) {
        query {
            AlbumTable
                .join(
                    otherTable = AlbumCountryRelationTable,
                    joinType = JoinType.INNER,
                    additionalConstraint = {
                        AlbumCountryRelationTable.albumId as Column<*> eq AlbumTable.id
                    }
                )
                .slice(
                    AlbumTable.id,
                    AlbumTable.name,
                    AlbumTable.points
                )
                .select {
                    AlbumCountryRelationTable.countryId eq countryId
                }
                .orderBy(AlbumTable.points to SortOrder.DESC)
                .limit(7)
                .map {
                    Pair(
                        first = it[AlbumTable.id].value,
                        second = it[AlbumTable.name]
                    )
                }.map { pair ->
                    async {
                        query {
                            SongTable
                                .join(
                                    otherTable = SongAlbumRelationTable,
                                    joinType = JoinType.INNER,
                                    additionalConstraint = {
                                        SongAlbumRelationTable.songId as Column<*> eq SongTable.id
                                    }
                                )
                                .slice(
                                    SongTable.coverImage
                                )
                                .select {
                                    SongAlbumRelationTable.albumId eq pair.first
                                }.limit(1)
                                .map {
                                    PrevAlbumDto(
                                        albumId = pair.first,
                                        name = pair.second,
                                        coverImage = it[SongTable.coverImage].constructSongCoverImage()
                                    )
                                }
                        }
                    }
                }.awaitAll()
                .flatten()
        }
    }

    override suspend fun getPopularArtist(
        userId: Long,
        userType: UserType,
        countryId: Int,
    ): List<ArtistDto> = withContext(Dispatchers.IO) {
        val sentArtistIdList = query {
            UserArtistRelationTable.select {
                UserArtistRelationTable.userId eq userId and (UserArtistRelationTable.userType eq userType.name)
            }.map {
                it[UserArtistRelationTable.artistId]
            }
        }

        query {
            ArtistTable
                .innerJoin(ArtistCountryRelationTable)
                .slice(
                    ArtistTable.id,
                    ArtistTable.name,
                    ArtistTable.points,
                    ArtistTable.profilePicUrl
                )
                .select {
                    ArtistCountryRelationTable.countryId eq countryId and
                            (ArtistTable.id notInList sentArtistIdList)
                }
                .orderBy(ArtistTable.points to SortOrder.DESC)
                .limit(7)
                .map {
                    ArtistDto(
                        id = it[ArtistTable.id].value,
                        name = it[ArtistTable.name],
                        coverImage = it[ArtistTable.profilePicUrl]?.constructArtistProfileUrl()
                    )
                }
        }
    }

    override suspend fun getPopularArtistSongPrev(
        userId: Long,
        userType: UserType,
        excludeArtist: List<Long>,
        countryId: Int,
    ): List<PreArtistSongDto> = withContext(Dispatchers.IO) {
        val sentArtistIdList = query {
            UserArtistRelationTable.select {
                UserArtistRelationTable.userId eq userId and (UserArtistRelationTable.userType eq userType.name)
            }.map {
                it[UserArtistRelationTable.artistId]
            }
        }

        query {
            ArtistTable.innerJoin(ArtistCountryRelationTable)
                .slice(
                    ArtistTable.id,
                    ArtistTable.name,
                    ArtistTable.points,
                    ArtistTable.profilePicUrl
                ).select {
                    ArtistCountryRelationTable.countryId eq countryId and
                            (ArtistTable.id notInList (excludeArtist + sentArtistIdList))
                }.orderBy(ArtistTable.points to SortOrder.DESC)
                .limit(5)
                .map {
                    ArtistDto(
                        id = it[ArtistTable.id].value,
                        name = it[ArtistTable.name],
                        coverImage = it[ArtistTable.profilePicUrl]?.constructArtistProfileUrl()
                    )
                }.map { artist ->
                    async {
                        artist to query {
                            SongTable
                                .innerJoin(SongArtistRelationTable)
                                .slice(
                                    SongTable.id,
                                    SongTable.coverImage,
                                    SongTable.title,
                                    SongTable.points
                                ).select {
                                    SongArtistRelationTable.artistId eq artist.id
                                }.orderBy(SongTable.points to SortOrder.DESC)
                                .limit(7)
                                .map {
                                    PrevSongDetailDto(
                                        songId = it[SongTable.id].value,
                                        coverImage = it[SongTable.coverImage].constructSongCoverImage(),
                                        title = it[SongTable.title],
                                        artist = ""
                                    )
                                }
                        }
                    }
                }.awaitAll()
                .map {
                    PreArtistSongDto(
                        artist = it.first,
                        songs = it.second
                    )
                }
        }
    }

    override suspend fun getPopularSongMix(
        countryId: Int,
    ): List<SongDto> = coroutineScope {
        query {
            getPopularSonMixQuery(countryId)
                .limit(kotlin.random.Random.nextInt(46, 56))
                .map {
                    async { it.toSongDto(database) }
                }.awaitAll()
        }
    }

    override suspend fun getOldGem(
        countryId: Int,
        year: Int,
    ): List<SongDto> = coroutineScope {
        (1..4).map { index ->
            async {
                val targetYear = year - 1 + index

                query {
                    getPopularSongFromUserTimeQuery(
                        year = targetYear,
                        countryId = countryId,
                    ).limit(kotlin.random.Random.nextInt(10, 14))
                        .map {
                            async { it.toSongDto(database) }
                        }.awaitAll()
                }
            }
        }.awaitAll().flatten()
    }

    override suspend fun getArtistSongMix(
        countryId: Int,
        userId: Long,
        userType: UserType,
    ): List<SongDto> = coroutineScope {
        query {
            getFavouriteArtistMixQuery(userId, userType.name)
                .limit(kotlin.random.Random.nextInt(45, 55))
                .map { async { it.toSongDto(database) } }.awaitAll()
        }
    }
}