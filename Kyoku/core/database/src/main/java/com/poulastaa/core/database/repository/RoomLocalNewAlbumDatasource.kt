package com.poulastaa.core.database.repository

import com.poulastaa.core.database.dao.CommonDao
import com.poulastaa.core.database.entity.relation.SongAlbumRelationEntity
import com.poulastaa.core.database.mapper.toAlbumEntity
import com.poulastaa.core.database.mapper.toSongEntity
import com.poulastaa.core.domain.model.AlbumWithSong
import com.poulastaa.core.domain.repository.new_album.LocalNewAlbumDataSource
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class RoomLocalNewAlbumDatasource @Inject constructor(
    private val commonDao: CommonDao
) : LocalNewAlbumDataSource {
    override suspend fun getNotSavedAlbumIdList(list: List<Long>): List<Long> {
        val savedAlbumIdList = commonDao.getAllSavedAlbum().first()
            .map { album -> album.albumId }

        return list.filterNot { savedAlbumIdList.contains(it) }
    }

    override suspend fun saveAlbums(list: List<AlbumWithSong>) {
        coroutineScope {
            list.map { data ->
                async {
                    val albumEntry = data.album.toAlbumEntity()
                    val songEntry = data.listOfSong.map { it.toSongEntity() }

                    val albumDef = async { commonDao.insertAlbum(albumEntry) }
                    val songDef = async { commonDao.insertSongs(songEntry) }

                    albumDef.await()
                    songDef.await()

                    val relation = songEntry.map { it.id }.map {
                        SongAlbumRelationEntity(
                            albumId = albumEntry.id,
                            songId = it
                        )
                    }

                    commonDao.insertSongAlbumRelation(relation)
                }
            }.awaitAll()
        }
    }
}