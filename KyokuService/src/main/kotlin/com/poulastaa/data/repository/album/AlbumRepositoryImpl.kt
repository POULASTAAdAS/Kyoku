package com.poulastaa.data.repository.album

import com.poulastaa.data.model.db_table.AlbumTable
import com.poulastaa.data.model.db_table.SongAlbumArtistRelationTable
import com.poulastaa.data.model.db_table.SongTable
import com.poulastaa.data.model.home.HomeResponseSong
import com.poulastaa.data.model.home.ResponseAlbumPreview
import com.poulastaa.domain.repository.album.AlbumRepository
import com.poulastaa.plugins.dbQuery
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.select

class AlbumRepositoryImpl : AlbumRepository {
    override suspend fun getResponseAlbumPreview(artistIdList: List<Int>): List<ResponseAlbumPreview> {
        val homeResponseSongList = dbQuery {
            SongTable
                .join(
                    SongAlbumArtistRelationTable,
                    JoinType.INNER,
                    additionalConstraint = {
                        SongTable.id eq SongAlbumArtistRelationTable.songId as Column<*>
                    }
                )
                .join(
                    otherTable = AlbumTable,
                    joinType = JoinType.INNER,
                    additionalConstraint = {
                        SongAlbumArtistRelationTable.albumId eq AlbumTable.id as Column<*>
                    }
                ).slice(
                    SongTable.id,
                    SongTable.title,
                    SongTable.coverImage,
                    SongTable.artist,
                    SongTable.album,
                    AlbumTable.points
                ).select {
                    AlbumTable.id inSubQuery (
                            SongAlbumArtistRelationTable
                                .slice(SongAlbumArtistRelationTable.albumId)
                                .select {
                                    SongAlbumArtistRelationTable.artistId inList artistIdList
                                }
                            )
                }.orderBy(
                    AlbumTable.points, SortOrder.DESC
                )
                .limit(60)
                .map {
                    HomeResponseSong(
                        it[SongTable.id].toString(),
                        it[SongTable.title],
                        it[SongTable.coverImage],
                        it[SongTable.artist],
                        it[SongTable.album]
                    )
                }
        }

        return homeResponseSongList.groupBy {
            it.album
        }.map {
            ResponseAlbumPreview(
                name = it.key,
                listOfSongs = it.value.take(2)
            )
        }.take(6)
    }
}