package com.poulastaa.core.domain.repository.explore_artist

import androidx.paging.PagingData
import com.poulastaa.core.domain.model.AlbumWithSong
import com.poulastaa.core.domain.model.Artist
import com.poulastaa.core.domain.model.ArtistSingleData
import com.poulastaa.core.domain.model.Song
import com.poulastaa.core.domain.utils.DataError
import com.poulastaa.core.domain.utils.EmptyResult
import com.poulastaa.core.domain.utils.Result
import kotlinx.coroutines.flow.Flow

interface RemoteExploreArtistDatasource {
    suspend fun getArtist(artistId: Long): Result<Artist, DataError.Network>

    suspend fun followArtist(artistId: Long): Result<Artist, DataError.Network>
    suspend fun unFollowArtist(artistId: Long): EmptyResult<DataError.Network>

    suspend fun getArtistSong(artistId: Long): Flow<PagingData<ArtistSingleData>>
    suspend fun getArtistAlbum(artistId: Long): Flow<PagingData<ArtistSingleData>>

    suspend fun saveAlbum(albumId: Long): Result<AlbumWithSong,DataError.Network>
    suspend fun addSongToFavourite(songId: Long): Result<Song, DataError.Network>
}