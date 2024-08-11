package com.poulastaa.play.data

import com.poulastaa.core.domain.home.HomeRepository
import com.poulastaa.core.domain.home.LocalHomeDatasource
import com.poulastaa.core.domain.home.RemoteHomeDatasource
import com.poulastaa.core.domain.model.DayType
import com.poulastaa.core.domain.model.HomeData
import com.poulastaa.core.domain.utils.DataError
import com.poulastaa.core.domain.utils.EmptyResult
import com.poulastaa.core.domain.utils.Result
import com.poulastaa.core.domain.utils.SavedAlbum
import com.poulastaa.core.domain.utils.SavedPlaylist
import com.poulastaa.core.domain.utils.asEmptyDataResult
import com.poulastaa.core.domain.utils.map
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OnlineFirstHomeRepository @Inject constructor(
    private val local: LocalHomeDatasource,
    private val remote: RemoteHomeDatasource,
    private val application: CoroutineScope,
) : HomeRepository {
    override suspend fun storeNewHomeResponse(dayType: DayType): EmptyResult<DataError.Network> {
        val result = remote.getNewHomeResponse(dayType)

        if (result is Result.Error) return result.asEmptyDataResult()

        application.async {
            result.map {
                local.storeNewHomeResponse(
                    dayType = dayType,
                    response = it
                )
            }
        }.await()

        return Result.Success(Unit)
    }

    override suspend fun isNewUser(): Boolean = local.isNewUser()

    override suspend fun loadHomeData(): HomeData = application.async {
        local.loadHomeStaticData()
    }.await()

    override fun loadSavedPlaylist(): Flow<SavedPlaylist> = local.loadSavedPlaylist()

    override fun loadSavedAlbum(): Flow<SavedAlbum> = local.loadSavedAlbum()

    override suspend fun isArtistIsInLibrary(artistId: Long): Boolean =
        local.isArtistIsInLibrary(artistId)

    override suspend fun isAlbumInLibrary(albumId: Long): Boolean = local.isAlbumInLibrary(albumId)
    override suspend fun isSongInFavourite(songId: Long): Boolean = local.isSongInFavourite(songId)

    override suspend fun insertIntoFavourite(id: Long): Boolean {
        // make api call get song
        val result = remote.insertIntoFavourite(id)

        return if (result is Result.Success) {
            if (local.isSongInDatabase(id)) {  // if song in database insert into favourite
                application.async { local.insertIntoFavourite(id) }.await()
                true
            } else { // else insert song then insert into favourite
                if (result.data.id == -1L) return false

                application.async { local.addSong(result.data) }.await()
                application.async { local.insertIntoFavourite(id) }.await()
                true
            }
        } else false
    }

    override suspend fun removeFromFavourite(id: Long): Boolean {
        val result = remote.removeFromFavourite(id)

        if (result is Result.Success) {
            application.async { local.removeSongFromFavourite(id) }.await()

            return true
        }

        return false
    }

    override suspend fun followArtist(id: Long): Boolean {
        val result = remote.followArtist(id)

        return if (result is Result.Success) {
            if (result.data.id == -1L) return false

            application.async { local.followArtist(result.data) }.await()
            true
        } else false
    }

    override suspend fun unFollowArtist(id: Long): Boolean {
        val result = remote.unFollowArtist(id)

        if (result is Result.Success) {
            application.async { local.unFollowArtist(id) }.await()
            return true
        }

        return false
    }

    override suspend fun saveAlbum(id: Long): Boolean {
        val result = remote.saveAlbum(id)

        if (result is Result.Success) {
            if (result.data.album.albumId == -1L) return false

            application.async { local.saveAlbum(result.data) }.await()
            return true
        }

        return false
    }

    override suspend fun removeAlbum(id: Long): Boolean {
        val result = remote.removeAlbum(id)

        if (result is Result.Success) {
            application.async { local.removeAlbum(id) }.await()
            return true
        }

        return false
    }
}