package com.poulastaa.data.repository

import com.poulastaa.data.dao.AlbumDao
import com.poulastaa.data.dao.ArtistDao
import com.poulastaa.data.dao.PlaylistDao
import com.poulastaa.data.dao.SongDao
import com.poulastaa.data.mappers.constructSongCoverImage
import com.poulastaa.data.mappers.toArtistResult
import com.poulastaa.data.mappers.toSongDto
import com.poulastaa.data.model.ArtistPagerDataDto
import com.poulastaa.data.model.ArtistSingleDataDto
import com.poulastaa.data.model.SongDto
import com.poulastaa.data.model.ViewArtistSongDto
import com.poulastaa.domain.model.ResultArtist
import com.poulastaa.domain.model.UserType
import com.poulastaa.domain.repository.DatabaseRepository
import com.poulastaa.domain.table.*
import com.poulastaa.domain.table.relation.*
import com.poulastaa.plugins.query
import kotlinx.coroutines.*
import org.jetbrains.exposed.sql.*
import java.time.LocalDate
import kotlin.random.Random

class KyokuDatabaseImpl : DatabaseRepository {
    override fun updateSongPointByOne(list: List<Long>) {
        CoroutineScope(Dispatchers.IO).launch {
            query {
                SongTable.update({ SongTable.id inList list }) {
                    with(SqlExpressionBuilder) {
                        it[points] = points + 1
                    }
                }
            }
        }
    }

    override fun updateArtistPointByOne(list: List<Long>) {
        CoroutineScope(Dispatchers.IO).launch {
            query {
                ArtistTable.update({ ArtistTable.id inList list }) {
                    with(SqlExpressionBuilder) {
                        it[points] = points + 1
                    }
                }
            }
        }
    }

    override suspend fun getArtistOnSongId(songId: Long): List<ResultArtist> = query {
        val artistIdList = SongArtistRelationTable.select {
            SongArtistRelationTable.songId eq songId
        }.map {
            it[SongArtistRelationTable.artistId]
        }

        ArtistDao.find {
            ArtistTable.id inList artistIdList
        }.map {
            it.toArtistResult()
        }
    }

    override suspend fun getArtistOnSongIdList(
        list: List<Long>,
    ): List<Pair<Long, List<ResultArtist>>> = coroutineScope {
        val idMap = list.map {
            async {
                it to query {
                    SongArtistRelationTable.select {
                        SongArtistRelationTable.songId eq it
                    }.map {
                        it[SongArtistRelationTable.artistId]
                    }
                }
            }
        }.awaitAll()

        idMap.map { pair ->
            async {
                pair.first to pair.second.let { artistIdList ->
                    query {
                        ArtistDao.find {
                            ArtistTable.id inList artistIdList
                        }.map {
                            it.toArtistResult()
                        }
                    }
                }
            }
        }.awaitAll()
    }

    override fun updateGenrePointByOne(list: List<Int>) {
        CoroutineScope(Dispatchers.IO).launch {
            query {
                GenreTable.update({ GenreTable.id inList list }) {
                    with(SqlExpressionBuilder) {
                        it[points] = points + 1
                    }
                }
            }
        }
    }

    override suspend fun getSongOnId(id: Long): SongDto = query {
        SongDao.find {
            SongTable.id eq id
        }.singleOrNull()?.let {
            val artist = getArtistOnSongId(it.id.value).joinToString { artist -> artist.name }

            it.toSongDto(artist)
        }
    } ?: SongDto()

    override suspend fun getSongOnIdList(list: List<Long>): List<SongDto> = coroutineScope {
        query {
            SongDao.find {
                SongTable.id inList list
            }.map {
                async {
                    val artist = getArtistOnSongId(it.id.value).joinToString { artist -> artist.name }

                    it.toSongDto(artist)
                }
            }.awaitAll()
        }
    }

    override suspend fun createPlaylist(
        name: String,
        userId: Long,
        userType: UserType,
        songIdList: List<Long>,
    ): Long = coroutineScope {
        val playlist = async {
            query {
                PlaylistDao.new {
                    this.name = name
                }
            }
        }.await()

        songIdList.map { id ->
            async {
                query {
                    UserPlaylistSongRelationTable.insertIgnore {
                        it[this.playlistId] = playlist.id.value
                        it[this.songId] = id
                        it[this.userType] = userType.name
                        it[this.userId] = userId
                    }
                }
            }
        }.awaitAll()

        playlist.id.value
    }

    override suspend fun getPlaylistOnId(id: Long): PlaylistDao? = query {
        PlaylistDao.find {
            PlaylistTable.id eq id
        }.firstOrNull()
    }

    override suspend fun getAlbumOnId(albumId: Long): AlbumDao? = query {
        AlbumDao.find {
            AlbumTable.id eq albumId
        }.firstOrNull()
    }

    override suspend fun getArtistOnId(artistId: Long): ArtistDao? = query {
        ArtistDao.find {
            ArtistTable.id eq artistId
        }.firstOrNull()
    }

    override suspend fun getPlaylistSong(
        playlistId: Long,
        userId: Long,
        userType: UserType,
    ): List<SongDto> = coroutineScope {
        query {
            UserPlaylistSongRelationTable
                .slice(UserPlaylistSongRelationTable.songId)
                .select {
                    UserPlaylistSongRelationTable.playlistId eq playlistId and
                            (UserPlaylistSongRelationTable.userId eq userId) and
                            (UserPlaylistSongRelationTable.userType eq userType.name)
                }.map { it[UserPlaylistSongRelationTable.songId] }.let {
                    SongDao.find {
                        SongTable.id inList it
                    }.map {
                        async {
                            val artist = getArtistOnSongId(it.id.value).joinToString { artist -> artist.name }
                            it.toSongDto(artist)
                        }
                    }.awaitAll()
                }
        }
    }


    override suspend fun getAlbumSong(
        albumId: Long,
    ): List<SongDto> = coroutineScope {
        query {
            SongAlbumRelationTable.slice(SongAlbumRelationTable.songId).select {
                SongAlbumRelationTable.albumId eq albumId
            }.map {
                it[SongAlbumRelationTable.songId]
            }.let {
                SongDao.find {
                    SongTable.id inList it
                }.map {
                    async {
                        val artist = getArtistOnSongId(it.id.value).joinToString { artist -> artist.name }
                        it.toSongDto(artist)
                    }
                }.awaitAll()
            }
        }
    }

    override suspend fun getArtistPopularity(artistId: Long): Long = query {
        UserArtistRelationTable.select {
            UserArtistRelationTable.artistId eq artistId
        }.count()
    }

    override suspend fun getMostPoplarArtistSongsPrev(artistId: Long): List<ViewArtistSongDto> = query {
        SongArtistRelationTable
            .join(
                otherTable = SongTable,
                joinType = JoinType.INNER,
                additionalConstraint = {
                    SongTable.id as Column<*> eq SongArtistRelationTable.songId
                }
            )
            .slice(
                SongTable.id,
                SongTable.title,
                SongTable.coverImage,
                SongTable.points,
            )
            .select {
                SongArtistRelationTable.artistId eq artistId
            }
            .orderBy(SongTable.points to SortOrder.DESC)
            .limit(Random.nextInt(15, 25))
            .map {
                ViewArtistSongDto(
                    id = it[SongTable.id].value,
                    title = it[SongTable.title],
                    coverImage = it[SongTable.coverImage].constructSongCoverImage(),
                    popularity = it[SongTable.points],
                )
            }
    }

    override suspend fun getArtistSongPagingData(
        artistId: Long,
        page: Int,
        size: Int,
    ): ArtistPagerDataDto = query {
        SongArtistRelationTable
            .join(
                otherTable = SongTable,
                joinType = JoinType.INNER,
                additionalConstraint = {
                    SongTable.id as Column<*> eq SongArtistRelationTable.songId
                }
            ).slice(
                SongTable.id,
                SongTable.title,
                SongTable.coverImage,
                SongTable.year
            ).select {
                SongArtistRelationTable.artistId eq artistId
            }.orderBy(SongTable.year to SortOrder.DESC)
            .map {
                ArtistSingleDataDto(
                    id = it[SongTable.id].value,
                    title = it[SongTable.title],
                    coverImage = it[SongTable.coverImage].constructSongCoverImage(),
                    releaseYear = it[SongTable.year]
                )
            }
            .drop(if (page == 1) 0 else page * size)
            .take(size)
            .let {
                ArtistPagerDataDto(list = it)
            }
    }

    override suspend fun getArtistAlbumPagingData(artistId: Long, page: Int, size: Int): ArtistPagerDataDto {
        val coverImage = Max(SongTable.coverImage, SongTable.coverImage.columnType)
        val year = Max(SongTable.year, SongTable.year.columnType)


        return query {
            ArtistAlbumRelationTable
                .join(
                    otherTable = AlbumTable,
                    joinType = JoinType.INNER,
                    additionalConstraint = {
                        AlbumTable.id as Column<*> eq ArtistAlbumRelationTable.albumId
                    }
                )
                .join(
                    otherTable = SongAlbumRelationTable,
                    joinType = JoinType.INNER,
                    additionalConstraint = {
                        SongAlbumRelationTable.albumId eq AlbumTable.id as Column<*>
                    }
                )
                .join(
                    otherTable = SongTable,
                    joinType = JoinType.INNER,
                    additionalConstraint = {
                        SongTable.id as Column<*> eq SongAlbumRelationTable.songId
                    }
                )
                .slice(
                    AlbumTable.id,
                    AlbumTable.name,
                    coverImage,
                    year
                ).select {
                    ArtistAlbumRelationTable.artistId eq artistId
                }.groupBy(AlbumTable.id)
                .orderBy(Max(SongTable.year, SongTable.year.columnType) to SortOrder.DESC)
                .map {
                    ArtistSingleDataDto(
                        id = it[AlbumTable.id].value,
                        title = it[AlbumTable.name],
                        coverImage = it[coverImage]?.constructSongCoverImage() ?: "",
                        releaseYear = it[year] ?: LocalDate.now().year
                    )
                }.drop(if (page == 1) 0 else page * size)
                .take(size)
                .let {
                    ArtistPagerDataDto(list = it)
                }
        }
    }
}