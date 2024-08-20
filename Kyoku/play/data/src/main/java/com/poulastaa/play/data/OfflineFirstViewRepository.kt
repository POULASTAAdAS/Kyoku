package com.poulastaa.play.data

import com.poulastaa.core.ViewData
import com.poulastaa.core.domain.model.PlaylistSong
import com.poulastaa.core.domain.view.LocalViewDatasource
import com.poulastaa.core.domain.view.RemoteViewDatasource
import com.poulastaa.core.domain.view.ViewRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import javax.inject.Inject

class OfflineFirstViewRepository @Inject constructor(
    private val local: LocalViewDatasource,
    private val remote: RemoteViewDatasource,
    private val application: CoroutineScope
) : ViewRepository {
    override suspend fun getPlaylistOnId(id: Long): ViewData {
        val localPlaylist = local.getPlaylistOnId(id)
        if (localPlaylist.listOfSong.isNotEmpty()) return localPlaylist

//        val remotePlaylist = remote.getPlaylistOnId(id)
//        application.async { local.savePlaylist(remotePlaylist) }.await()
        return ViewData()
    }

    override suspend fun getAlbumOnId(id: Long): ViewData {
        val localAlbum = local.getAlbumOnId(id)
        if (localAlbum.listOfSong.isNotEmpty()) return localAlbum

//        val remoteAlbum = remote.getAlbumOnId(id)
//        application.async { local.saveAlbum(remoteAlbum) }.await()
        return ViewData()
    }

    override suspend fun getFev(): List<PlaylistSong> {
        val songIdList = local.getFevSongIdList()
        if (songIdList.isEmpty()) return emptyList()


        val foundSongList = local.getSongOnIdList(songIdList)
        val notFoundIdList =
            songIdList.filterNot { it in foundSongList.map { entry -> entry.id } }

        if (notFoundIdList.isEmpty()) return foundSongList

        val responseSong = remote.getSongOnIdList(notFoundIdList)
        application.async { local.saveSongs(responseSong) }.await()

//        return foundSongList + responseSong.map {  }

        return foundSongList // todo change
    }

    override suspend fun getOldMix(): List<PlaylistSong> {
        return listOf()
    }

    override suspend fun getArtistMix(): List<PlaylistSong> {
        val songIdList = local.getSongIdList(LocalViewDatasource.ReqType.ARTIST_MIX)
        return if (songIdList.isEmpty()) {
            val prevSongIdList =
                application.async { local.getSongIdList(LocalViewDatasource.ReqType.ARTIST_MIX) }
                    .await()
            val songList = remote.getArtistMix(prevSongIdList)
            application.async { local.saveSongs(songList) }.await()

            emptyList()
        } else {
            val foundSongList = local.getSongOnIdList(songIdList)

            val notFoundIdList =
                songIdList.filterNot { it in foundSongList.map { entry -> entry.id } }
            if (notFoundIdList.isEmpty()) return foundSongList

            val responseSong = remote.getSongOnIdList(notFoundIdList)
            application.async { local.saveSongs(responseSong) }.await()
//            return foundSongList + responseSong

            emptyList()
        }
    }

    override suspend fun getPopularMix(): List<PlaylistSong> {
        return listOf()
    }
}