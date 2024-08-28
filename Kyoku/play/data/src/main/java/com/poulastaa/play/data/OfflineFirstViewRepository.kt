package com.poulastaa.play.data

import com.poulastaa.core.ViewData
import com.poulastaa.core.domain.model.PlaylistSong
import com.poulastaa.core.domain.repository.view.LocalViewDatasource
import com.poulastaa.core.domain.repository.view.RemoteViewDatasource
import com.poulastaa.core.domain.repository.view.ViewRepository
import com.poulastaa.core.domain.utils.DataError
import com.poulastaa.core.domain.utils.EmptyResult
import com.poulastaa.core.domain.utils.Result
import com.poulastaa.core.domain.utils.asEmptyDataResult
import com.poulastaa.core.domain.utils.map
import com.poulastaa.play.data.mapper.toPlaylistSong
import com.poulastaa.play.data.mapper.toViewData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class OfflineFirstViewRepository @Inject constructor(
    private val local: LocalViewDatasource,
    private val remote: RemoteViewDatasource,
    private val application: CoroutineScope
) : ViewRepository {
    override suspend fun getPlaylistOnId(id: Long): Result<ViewData, DataError.Network> {
        val localPlaylist = local.getPlaylistOnId(id)
        if (localPlaylist.listOfSong.isNotEmpty()) return Result.Success(localPlaylist)

        val remotePlaylist = remote.getPlaylistOnId(id)
        return remotePlaylist.map { it.toViewData() }
    }

    override suspend fun getAlbumOnId(id: Long): Result<ViewData, DataError.Network> {
        val localAlbum = local.getAlbumOnId(id)
        if (localAlbum.listOfSong.isNotEmpty()) return Result.Success(localAlbum)

        return coroutineScope {
            val remoteAlbumDef = async { remote.getAlbumOnId(id) }
            val isAlbumOnLibrary = async { local.isAlbumOnLibrary(id) }

            val remoteAlbum = remoteAlbumDef.await()

            if (isAlbumOnLibrary.await() && remoteAlbum is Result.Success)
                local.saveAlbum(remoteAlbum.data)

            remoteAlbum.map { it.toViewData() }
        }
    }

    override suspend fun isSavedAlbum(id: Long): Boolean =
        local.isAlbumOnLibrary(id)

    override suspend fun isSongInFavourite(songId: Long) = local.isSongInFavourite(songId)

    override suspend fun getFev(): Result<List<PlaylistSong>, DataError.Network> {
        val songIdList = local.getFevSongIdList()
        if (songIdList.isEmpty()) return Result.Success(emptyList())

        val foundSongList = local.getSongOnIdList(songIdList)

        val notFoundIdList =
            songIdList.filterNot { it in foundSongList.map { entry -> entry.id } }

        if (notFoundIdList.isEmpty()) return Result.Success(foundSongList)

        val responseSong = remote.getSongOnIdList(notFoundIdList)
        if (responseSong is Result.Success) application.async {
            local.insertSongs(
                responseSong.data,
                LocalViewDatasource.ReqType.FEV
            )
        }.await()

        return responseSong.map { foundSongList + it.map { song -> song.toPlaylistSong() } }
    }

    override suspend fun getOldMix(): Result<List<PlaylistSong>, DataError.Network> {
        val songIdList = local.getSongIdList(LocalViewDatasource.ReqType.OLD_MIX_SONG)

        return if (songIdList.isEmpty()) {
            val prevSongIdList =
                application.async { local.getSongIdList(LocalViewDatasource.ReqType.OLD_MIX_SONG) }
                    .await()
            val songList = remote.getOldMix(prevSongIdList)

            if (songList is Result.Success) application.async {
                local.insertSongs(
                    songList.data,
                    LocalViewDatasource.ReqType.OLD_MIX_SONG
                )
            }.await()

            songList.map { it.map { song -> song.toPlaylistSong() } }
        } else {
            val foundSongList = local.getSongOnIdList(songIdList)

            val notFoundIdList =
                songIdList.filterNot { it in foundSongList.map { entry -> entry.id } }
            if (notFoundIdList.isEmpty()) return Result.Success(foundSongList)

            val responseSong = remote.getSongOnIdList(notFoundIdList)

            if (responseSong is Result.Success)
                application.async { local.insertSongs(responseSong.data) }.await()

            responseSong.map { foundSongList + it.map { song -> song.toPlaylistSong() } }
        }
    }

    override suspend fun getArtistMix(): Result<List<PlaylistSong>, DataError.Network> {
        val songIdList = local.getSongIdList(LocalViewDatasource.ReqType.ARTIST_MIX)

        return if (songIdList.isEmpty()) {
            val prevSongIdList =
                application.async { local.getSongIdList(LocalViewDatasource.ReqType.ARTIST_MIX) }
                    .await()
            val songList = remote.getArtistMix(prevSongIdList)

            if (songList is Result.Success) application.async {
                local.insertSongs(
                    songList.data,
                    LocalViewDatasource.ReqType.ARTIST_MIX
                )
            }.await()

            songList.map { it.map { song -> song.toPlaylistSong() } }
        } else {
            val foundSongList = local.getSongOnIdList(songIdList)

            val notFoundIdList =
                songIdList.filterNot { it in foundSongList.map { entry -> entry.id } }
            if (notFoundIdList.isEmpty()) return Result.Success(foundSongList)

            val responseSong = remote.getSongOnIdList(notFoundIdList)
            if (responseSong is Result.Success) application.async { local.insertSongs(responseSong.data) }
                .await()

            responseSong.map { foundSongList + it.map { song -> song.toPlaylistSong() } }
        }
    }

    override suspend fun getPopularMix(): Result<List<PlaylistSong>, DataError.Network> {
        val songIdList = local.getSongIdList(LocalViewDatasource.ReqType.POPULAR_MIX)

        return if (songIdList.isEmpty()) {
            val prevSongIdList =
                application.async { local.getSongIdList(LocalViewDatasource.ReqType.POPULAR_MIX) }
                    .await()

            val songList = remote.getPopularMix(prevSongIdList)
            if (songList is Result.Success) application.async {
                local.insertSongs(
                    songList.data,
                    LocalViewDatasource.ReqType.POPULAR_MIX
                )
            }.await()

            return songList.map { it.map { song -> song.toPlaylistSong() } }
        } else {
            val foundSongList = local.getSongOnIdList(songIdList)

            val notFoundIdList =
                songIdList.filterNot { it in foundSongList.map { entry -> entry.id } }
            if (notFoundIdList.isEmpty()) return Result.Success(foundSongList)

            val responseSong = remote.getSongOnIdList(notFoundIdList)
            if (responseSong is Result.Success)
                application.async { local.insertSongs(responseSong.data) }.await()

            responseSong.map { foundSongList + it.map { song -> song.toPlaylistSong() } }
        }
    }

    override suspend fun addSongToFavourite(songId: Long): EmptyResult<DataError.Network> {
        val result = remote.addSongToFavourite(songId)
        if (result is Result.Success) application.async {
            local.insertSongs(
                listOf(result.data),
                LocalViewDatasource.ReqType.FEV
            )
        }.await()

        return result.asEmptyDataResult()
    }
}