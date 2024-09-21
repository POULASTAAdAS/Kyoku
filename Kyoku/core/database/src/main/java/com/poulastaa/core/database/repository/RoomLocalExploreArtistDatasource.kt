package com.poulastaa.core.database.repository

import com.poulastaa.core.database.dao.CommonDao
import com.poulastaa.core.database.dao.ViewDao
import com.poulastaa.core.database.entity.FavouriteEntity
import com.poulastaa.core.database.entity.relation.SongAlbumRelationEntity
import com.poulastaa.core.database.mapper.toAlbumEntity
import com.poulastaa.core.database.mapper.toArtist
import com.poulastaa.core.database.mapper.toArtistEntity
import com.poulastaa.core.database.mapper.toSongEntity
import com.poulastaa.core.domain.model.AlbumWithSong
import com.poulastaa.core.domain.model.Artist
import com.poulastaa.core.domain.model.Song
import com.poulastaa.core.domain.repository.explore_artist.LocalExploreArtistDatasource
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class RoomLocalExploreArtistDatasource @Inject constructor(
    private val commonDao: CommonDao,
    private val viewDao: ViewDao,
) : LocalExploreArtistDatasource {
    override suspend fun getArtist(artistId: Long): Artist? =
        commonDao.getArtistByIdOrNull(artistId)?.toArtist()

    override suspend fun isSongInFavourite(songId: Long): Boolean =
        commonDao.isSongInFavourite(songId) != null

    override suspend fun isAlbumSaved(albumId: Long): Boolean =
        viewDao.getAlbumOnId(albumId) != null

    override suspend fun followArtist(artist: Artist) =
        commonDao.insertArtist(artist.toArtistEntity())

    override suspend fun unFollowArtist(artistId: Long) = commonDao.deleteArtist(artistId)

    override suspend fun saveAlbum(data: AlbumWithSong) {
        coroutineScope {
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
    }

    override suspend fun addSongToFavourite(song: Song) {
        coroutineScope {
            async { commonDao.insertSong(song.toSongEntity()) }.await()
            commonDao.insertOneIntoFavourite(FavouriteEntity(song.id))
        }
    }
}