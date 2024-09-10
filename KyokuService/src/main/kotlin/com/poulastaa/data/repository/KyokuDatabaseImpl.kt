package com.poulastaa.data.repository

import com.poulastaa.data.dao.AlbumDao
import com.poulastaa.data.dao.ArtistDao
import com.poulastaa.data.dao.PlaylistDao
import com.poulastaa.data.dao.SongDao
import com.poulastaa.data.mappers.*
import com.poulastaa.data.model.*
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

        if (songIdList.firstOrNull() == -1L) return@coroutineScope playlist.id.value

        CoroutineScope(Dispatchers.IO).launch {
            query {
                UserPlaylistRelationTable.insertIgnore {
                    it[this.playlistId] = playlist.id.value
                    it[this.userType] = userType.name
                    it[this.userId] = userId
                }
            }
        }

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
        savedSongList: List<Long>,
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
                SongArtistRelationTable.artistId eq artistId and
                        (SongArtistRelationTable.songId notInList savedSongList)
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

    override suspend fun getAlbumPaging(
        page: Int,
        size: Int,
        query: String,
        type: AlbumPagingTypeDto,
    ): List<PagingAlbumDto> {
        return coroutineScope {
            when (type) {
                AlbumPagingTypeDto.NAME -> {
                    return@coroutineScope if (query.isNotBlank()) query {
                        AlbumDao.find {
                            AlbumTable.name like "$query%"
                        }.orderBy(AlbumTable.points to SortOrder.DESC)
                            .drop(if (page == 1) 0 else page * size)
                            .take(size).toList()
                    }.map { album ->
                        album.toPagingAlbumDto()
                    }.awaitAll()
                    else query {
                        AlbumDao.all()
                            .orderBy(AlbumTable.name to SortOrder.ASC)
                            .drop(if (page == 1) 0 else page * size)
                            .take(size).toList()
                    }.map { album ->
                        album.toPagingAlbumDto()
                    }.awaitAll()
                }

                AlbumPagingTypeDto.BY_YEAR -> {
                    query {
                        val fieldSet = AlbumTable.join(
                            otherTable = SongAlbumRelationTable,
                            joinType = JoinType.INNER,
                            additionalConstraint = {
                                SongAlbumRelationTable.albumId eq AlbumTable.id as Column<*>
                            }
                        ).join(
                            otherTable = SongTable,
                            joinType = JoinType.INNER,
                            additionalConstraint = {
                                SongAlbumRelationTable.songId eq SongTable.id as Column<*>
                            }
                        ).slice(
                            AlbumTable.id,
                            AlbumTable.name,
                            SongTable.coverImage,
                            SongTable.year
                        )

                        val valueSet = if (query.isEmpty()) fieldSet.selectAll()
                        else fieldSet.select {
                            AlbumTable.name like "$query%"
                        }

                        valueSet
                            .orderBy(SongTable.year to SortOrder.DESC)
                            .drop(if (page == 1) 0 else page * size)
                            .take(size)
                            .map { res ->
                                AlbumDto(
                                    id = res[AlbumTable.id].value,
                                    name = res[AlbumTable.name],
                                    coverImage = res[SongTable.coverImage].constructSongCoverImage(),
                                ) to res[SongTable.year]
                            }.map { (album, year) ->
                                async {
                                    val artist = async { getArtistOnAlbumId(albumId = album.id) }

                                    PagingAlbumDto(
                                        id = album.id,
                                        name = album.name,
                                        coverImage = album.coverImage,
                                        artist = artist.await(),
                                        releaseYear = year.toString()
                                    )
                                }
                            }.awaitAll()
                    }
                }

                AlbumPagingTypeDto.BY_POPULARITY -> {
                    query {
                        if (query.isEmpty()) AlbumDao.all()
                            .orderBy(AlbumTable.points to SortOrder.DESC)
                            .drop(if (page == 1) 0 else page * size)
                            .take(size)
                            .toList()
                        else AlbumDao.find {
                            AlbumTable.name like "$query%"
                        }.orderBy(AlbumTable.points to SortOrder.DESC)
                            .drop(if (page == 1) 0 else page * size)
                            .take(size)
                    }.map { album ->
                        album.toPagingAlbumDto()
                    }.awaitAll()
                }
            }
        }
    }

    private suspend fun getArtistOnAlbumId(albumId: Long) = query {
        ArtistAlbumRelationTable.select {
            ArtistAlbumRelationTable.albumId eq albumId
        }.map {
            it[ArtistAlbumRelationTable.artistId]
        }.let {
            ArtistDao.find {
                ArtistTable.id inList it
            }.joinToString {
                it.name
            }
        }
    }

    override suspend fun getArtistPaging(
        page: Int,
        size: Int,
        query: String,
        countryId: Int,
        type: ArtistPagingTypeDto,
    ): List<ArtistDto> = coroutineScope {
        when (type) {
            ArtistPagingTypeDto.ALL -> {
                query {
                    ArtistDao.find {
                        ArtistTable.name like "$query%"
                    }.orderBy(ArtistTable.points to SortOrder.DESC)
                        .drop(if (page == 1) 0 else page * size)
                        .take(size)
                        .map {
                            it.toArtistDto()
                        }
                }
            }

            ArtistPagingTypeDto.INTERNATIONAL -> {
                query {
                    ArtistTable
                        .join(
                            otherTable = ArtistCountryRelationTable,
                            joinType = JoinType.INNER,
                            additionalConstraint = {
                                ArtistTable.id as Column<*> eq ArtistCountryRelationTable.artistId
                            }
                        )
                        .slice(
                            ArtistTable.id,
                            ArtistTable.name,
                            ArtistTable.profilePicUrl,
                            ArtistTable.points
                        ).select {
                            ArtistTable.name like "$query%" and
                                    (ArtistCountryRelationTable.countryId neq countryId)
                        }
                        .orderBy(ArtistTable.points to SortOrder.DESC)
                        .drop(if (page == 1) 0 else page * size)
                        .take(size)
                        .map {
                            ArtistDto(
                                id = it[ArtistTable.id].value,
                                name = it[ArtistTable.name],
                                coverImage = it[ArtistTable.profilePicUrl]?.constructArtistProfileUrl() ?: ""
                            )
                        }
                }
            }
        }
    }

    override suspend fun getCreatePlaylistData(
        userId: Long,
        userType: UserType,
        countryId: Int,
    ): CreatePlaylistDto = coroutineScope {
        val favouriteDef = async {
            query {
                UserFavouriteRelationTable
                    .slice(UserFavouriteRelationTable.songId)
                    .select {
                        UserFavouriteRelationTable.userId eq userId and
                                (UserFavouriteRelationTable.userType eq userType.name)
                    }.limit(Random.nextInt(30, 35))
                    .map { it[UserFavouriteRelationTable.songId] }
            }.let { getSongOnIdList(it) }
        }

        val recentHistoryDef = async {
            emptyList<SongDto>()
        } // todo

        val internationalDef = async {
            val albumIdDef = async {
                query {
                    AlbumCountryRelationTable
                        .slice(AlbumCountryRelationTable.albumId)
                        .select {
                            AlbumCountryRelationTable.countryId neq countryId
                        }
                        .limit(Random.nextInt(15, 20))
                        .map { it[AlbumCountryRelationTable.albumId] }
                }
            }
            val artistDef = async {
                query {
                    ArtistCountryRelationTable
                        .slice(ArtistCountryRelationTable.artistId)
                        .select {
                            ArtistCountryRelationTable.countryId neq countryId
                        }
                        .limit(Random.nextInt(10, 15))
                        .map { it[ArtistCountryRelationTable.artistId] }
                }
            }

            val artistSongDef = async {
                query {
                    SongArtistRelationTable
                        .slice(SongArtistRelationTable.songId)
                        .select {
                            SongArtistRelationTable.artistId inList artistDef.await()
                        }
                        .orderBy(org.jetbrains.exposed.sql.Random())
                        .limit(Random.nextInt(30, 40))
                        .map { it[SongArtistRelationTable.songId] }
                        .let { getSongOnIdList(it) }
                }
            }
            val albumSongDef = async {
                query {
                    SongAlbumRelationTable
                        .slice(SongAlbumRelationTable.songId)
                        .select {
                            SongAlbumRelationTable.albumId inList albumIdDef.await()
                        }
                        .orderBy(org.jetbrains.exposed.sql.Random())
                        .limit(Random.nextInt(30, 40))
                        .map { it[SongAlbumRelationTable.songId] }
                        .let { getSongOnIdList(it) }
                }
            }

            val artistSong = artistSongDef.await()
            val albumSong = albumSongDef.await()

            albumSong.filterNot { artistSong.contains(it) }
                .shuffled(Random)
                .take(Random.nextInt(35, 40))
        }

        val mostPopularDef = async {
            query {
                SongDao.all()
                    .orderBy(SongTable.points to SortOrder.DESC)
                    .limit(Random.nextInt(35, 45))
                    .map { dao ->
                        async {
                            val artist = getArtistOnSongId(dao.id.value)
                            dao.toSongDto(artist.joinToString { it.name })
                        }
                    }.awaitAll()
            }
        }


        val fevPair = CreatePlaylistTypeDto.YOUR_FAVOURITES to favouriteDef.await()
        val suggestPair = CreatePlaylistTypeDto.SUGGESTED_FOR_YOU to mostPopularDef.await()
        val likePair = CreatePlaylistTypeDto.YOU_MAY_ALSO_LIKE to internationalDef.await()
        val historyPair = CreatePlaylistTypeDto.RECENT_HISTORY to recentHistoryDef.await()

        val newFevPair =
            if (fevPair.second.size < 20) CreatePlaylistTypeDto.YOUR_FAVOURITES to
                    fevPair.second + (suggestPair.second + likePair.second + historyPair.second).shuffled(Random)
                .take(Random.nextInt(18, 25))
            else fevPair

        val data = listOf(
            newFevPair,
            suggestPair,
            likePair,
            historyPair
        )

        CreatePlaylistDto(data = data)
    }

    override suspend fun getSongPaging(
        page: Int,
        size: Int,
        query: String,
        type: SongPagingTypeDto,
    ): List<SongDto> = coroutineScope {
        when (type) {
            SongPagingTypeDto.TITLE -> {
                query {
                    SongDao.find {
                        SongTable.title like "$query%"
                    }.orderBy(SongTable.year to SortOrder.DESC)
                        .drop(if (page == 1) 0 else page * size)
                        .take(size)
                        .toList()
                }.map { dao ->
                    async {
                        val artist = getArtistOnSongId(dao.id.value)
                        dao.toSongDto(artist.joinToString { it.name })
                    }
                }.awaitAll()
            }

            SongPagingTypeDto.POPULARITY -> {
                query {
                    SongDao.find {
                        SongTable.title like "$query%"
                    }.orderBy(SongTable.year to SortOrder.DESC, SongTable.points to SortOrder.DESC)
                        .drop(if (page == 1) 0 else page * size)
                        .take(size)
                        .toList()
                }.map { dao ->
                    async {
                        val artist = getArtistOnSongId(dao.id.value)
                        dao.toSongDto(artist.joinToString { it.name })
                    }
                }.awaitAll()
            }

            SongPagingTypeDto.ARTIST -> {
                query {
                    SongTable
                        .join(
                            otherTable = SongArtistRelationTable,
                            joinType = JoinType.INNER,
                            additionalConstraint = {
                                SongArtistRelationTable.songId eq SongTable.id as Column<*>
                            }
                        ).join(
                            otherTable = ArtistTable,
                            joinType = JoinType.INNER,
                            additionalConstraint = {
                                SongArtistRelationTable.artistId eq ArtistTable.id as Column<*>
                            }
                        )
                        .slice(
                            SongTable.id,
                            SongTable.title,
                            SongTable.coverImage,
                            SongTable.masterPlaylistPath,
                            SongTable.year,
                            SongTable.points,
                        )
                        .select {
                            (ArtistTable.name like "$query%")
                        }.drop(if (page == 1) 0 else page * size)
                        .take(size)
                        .map { resultRow ->
                            async {
                                val artist =
                                    getArtistOnSongId(resultRow[SongTable.id].value).sortedBy { it.name.contains(query) }

                                SongDto(
                                    id = resultRow[SongTable.id].value,
                                    title = resultRow[SongTable.title],
                                    coverImage = resultRow[SongTable.coverImage].constructSongCoverImage(),
                                    releaseYear = resultRow[SongTable.year],
                                    artistName = artist.joinToString { it.name },
                                    masterPlaylistUrl = resultRow[SongTable.masterPlaylistPath].constructMasterPlaylistUrl()
                                )
                            }
                        }.awaitAll()
                }
            }
        }
    }

    private suspend fun AlbumDao.toPagingAlbumDto() =
        coroutineScope {
            async {
                val pair = async {
                    query {
                        SongAlbumRelationTable.join(
                            otherTable = SongTable,
                            joinType = JoinType.INNER,
                            additionalConstraint = {
                                SongTable.id as Column<*> eq SongAlbumRelationTable.songId
                            }
                        ).slice(
                            SongTable.coverImage,
                            SongTable.year
                        ).select {
                            SongAlbumRelationTable.albumId eq this@toPagingAlbumDto.id.value
                        }.first().let {
                            it[SongTable.coverImage] to it[SongTable.year]
                        }
                    }
                }

                val artistDef = async { getArtistOnAlbumId(albumId = this@toPagingAlbumDto.id.value) }

                val (cover, year) = pair.await()
                val artist = artistDef.await()

                PagingAlbumDto(
                    id = this@toPagingAlbumDto.id.value,
                    name = this@toPagingAlbumDto.name,
                    coverImage = cover.constructSongCoverImage(),
                    artist = artist,
                    releaseYear = year.toString()
                )
            }
        }
}