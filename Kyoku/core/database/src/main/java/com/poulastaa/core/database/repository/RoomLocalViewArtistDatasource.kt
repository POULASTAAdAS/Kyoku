package com.poulastaa.core.database.repository

import com.poulastaa.core.database.dao.CommonDao
import com.poulastaa.core.database.entity.FavouriteEntity
import com.poulastaa.core.database.mapper.toArtist
import com.poulastaa.core.database.mapper.toArtistEntity
import com.poulastaa.core.database.mapper.toSongEntity
import com.poulastaa.core.domain.model.Artist
import com.poulastaa.core.domain.model.Song
import com.poulastaa.core.domain.repository.view_artist.LocalViewArtistDatasource
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class RoomLocalViewArtistDatasource @Inject constructor(
    private val commonDao: CommonDao,
) : LocalViewArtistDatasource {
    override suspend fun getArtist(artistId: Long): Artist? =
        commonDao.getArtistByIdOrNull(artistId)?.toArtist()

    override suspend fun isSongInFavourite(songId: Long): Boolean =
        commonDao.isSongInFavourite(songId) != null

    override suspend fun followArtist(artist: Artist) =
        commonDao.insertArtist(artist.toArtistEntity())

    override suspend fun unFollowArtist(artistId: Long) = commonDao.deleteArtist(artistId)

    override suspend fun addSongToFavourite(song: Song) {
        coroutineScope {
            async { commonDao.insertSong(song.toSongEntity()) }.await()
            commonDao.insertOneIntoFavourite(FavouriteEntity(song.id))
        }
    }
}