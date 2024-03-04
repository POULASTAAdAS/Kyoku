package com.poulastaa.data.repository.album

import com.poulastaa.data.model.db_table.AlbumTable
import com.poulastaa.data.model.db_table.SongAlbumArtistRelationTable
import com.poulastaa.data.model.db_table.SongTable
import com.poulastaa.data.model.home.AlbumPreview
import com.poulastaa.data.model.home.ResponseAlbumPreview
import com.poulastaa.domain.repository.album.AlbumRepository
import com.poulastaa.plugins.dbQuery
import com.poulastaa.utils.constructCoverPhotoUrl
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.select

class AlbumRepositoryImpl : AlbumRepository {
    override suspend fun getResponseAlbumPreview(artistIdList: List<Int>) =
        ResponseAlbumPreview(
            listOfPreviewAlbum = dbQuery {
                SongTable
                    .join(
                        otherTable = SongAlbumArtistRelationTable,
                        joinType = JoinType.INNER,
                        additionalConstraint = {
                            SongTable.id eq SongAlbumArtistRelationTable.songId as Column<*>
                        }
                    ).join(
                        otherTable = AlbumTable,
                        joinType = JoinType.INNER,
                        additionalConstraint = {
                            SongAlbumArtistRelationTable.albumId as Column<*> eq AlbumTable.id
                        }
                    ).slice(
                        SongTable.title,
                        SongTable.coverImage,
                        SongTable.artist,
                        SongTable.album
                    ).select {
                        AlbumTable.id inSubQuery (
                                SongAlbumArtistRelationTable
                                    .slice(SongAlbumArtistRelationTable.albumId)
                                    .select {
                                        SongAlbumArtistRelationTable.artistId inList artistIdList
                                    }
                                )
                    }.orderBy(AlbumTable.points, SortOrder.DESC)
                    .limit(60)
                    .asSequence()
                    .map {
                        AlbumPreview(
                            name = it[SongTable.album],
                            coverImage = it[SongTable.coverImage].constructCoverPhotoUrl(),
                            title = it[SongTable.title],
                            artist = it[SongTable.artist]
                        )
                    }.groupBy {
                        it.name
                    }.map {
                        it.value.take(1)
                    }.flatten()
                    .take(5)
                    .toList()
            }
        )
}