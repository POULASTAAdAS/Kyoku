package com.poulastaa.core.domain.view

import com.poulastaa.core.domain.model.AlbumWithSong
import com.poulastaa.core.domain.model.PlaylistData
import com.poulastaa.core.domain.model.Song
import com.poulastaa.core.domain.utils.DataError
import com.poulastaa.core.domain.utils.Result


interface RemoteViewDatasource {
    suspend fun getPlaylistOnId(id: Long): Result<PlaylistData, DataError.Network>
    suspend fun getAlbumOnId(id: Long): Result<AlbumWithSong, DataError.Network>

    suspend fun getFev(): Result<List<Song>, DataError.Network>
    suspend fun getOldMix(prevList: List<Long>): Result<List<Song>, DataError.Network>
    suspend fun getArtistMix(prevList: List<Long>): Result<List<Song>, DataError.Network>
    suspend fun getPopularMix(prevList: List<Long>): Result<List<Song>, DataError.Network>

    suspend fun getSongOnIdList(list: List<Long>): Result<List<Song>, DataError.Network>
}