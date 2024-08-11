package com.poulastaa.core.domain.home

import com.poulastaa.core.domain.model.AlbumWithSong
import com.poulastaa.core.domain.model.Artist
import com.poulastaa.core.domain.model.DayType
import com.poulastaa.core.domain.model.NewHome
import com.poulastaa.core.domain.model.Song
import com.poulastaa.core.domain.utils.DataError
import com.poulastaa.core.domain.utils.EmptyResult
import com.poulastaa.core.domain.utils.Result

interface RemoteHomeDatasource {
    suspend fun getNewHomeResponse(dayType: DayType): Result<NewHome, DataError.Network>

    suspend fun insertIntoFavourite(id: Long): Result<Song, DataError.Network>
    suspend fun removeFromFavourite(id: Long): EmptyResult<DataError.Network>

    suspend fun followArtist(id: Long): Result<Artist, DataError.Network>
    suspend fun unFollowArtist(id: Long): EmptyResult<DataError.Network>

    suspend fun saveAlbum(id: Long): Result<AlbumWithSong, DataError.Network>
    suspend fun removeAlbum(id: Long): EmptyResult<DataError.Network>
}