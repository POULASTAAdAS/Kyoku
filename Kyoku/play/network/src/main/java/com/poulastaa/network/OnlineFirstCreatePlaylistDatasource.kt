package com.poulastaa.network

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.google.gson.Gson
import com.poulastaa.core.data.model.SongDto
import com.poulastaa.core.data.network.get
import com.poulastaa.core.data.network.post
import com.poulastaa.core.domain.EndPoints
import com.poulastaa.core.domain.model.CreatePlaylistPagerFilterType
import com.poulastaa.core.domain.model.CreatePlaylistPagingData
import com.poulastaa.core.domain.model.CreatePlaylistType
import com.poulastaa.core.domain.model.Song
import com.poulastaa.core.domain.repository.create_playlist.RemoteCreatePlaylistDatasource
import com.poulastaa.core.domain.utils.DataError
import com.poulastaa.core.domain.utils.EmptyResult
import com.poulastaa.core.domain.utils.Result
import com.poulastaa.core.domain.utils.asEmptyDataResult
import com.poulastaa.core.domain.utils.map
import com.poulastaa.network.mapper.toCreatePlaylistData
import com.poulastaa.network.mapper.toSong
import com.poulastaa.network.model.CreatePlaylistDto
import com.poulastaa.network.model.UpdatePlaylistReq
import com.poulastaa.network.paging_source.CreatePlaylistPagerSource
import kotlinx.coroutines.flow.Flow
import okhttp3.OkHttpClient
import javax.inject.Inject

class OnlineFirstCreatePlaylistDatasource @Inject constructor(
    private val client: OkHttpClient,
    private val gson: Gson,
    private val pager: CreatePlaylistPagerSource
) : RemoteCreatePlaylistDatasource {
    override suspend fun getStaticData(): Result<List<Pair<CreatePlaylistType, List<Song>>>, DataError.Network> =
        client.get<CreatePlaylistDto>(
            route = EndPoints.GetCreatePlaylistData.route,
            params = listOf(),
            gson = gson
        ).map { dto ->
            dto.data.map { it.toCreatePlaylistData() }
        }

    override suspend fun getPagingSong(
        query: String,
        type: CreatePlaylistPagerFilterType,
        savedSongIdList: List<Long>
    ): Flow<PagingData<CreatePlaylistPagingData>> {
        pager.init(query, type, savedSongIdList)

        return Pager(
            config = PagingConfig(
                pageSize = 10
            ),
            initialKey = 1,
            pagingSourceFactory = { pager }
        ).flow
    }

    override suspend fun getSong(
        songId: Long
    ): Result<Song, DataError.Network> = client.get<SongDto>(
        route = EndPoints.GetSong.route,
        params = listOf(
            Pair(
                first = "songId",
                second = songId.toString()
            )
        ),
        gson = gson
    ).map { it.toSong() }

    override suspend fun saveSong(
        songId: Long,
        playlistId: Long
    ): EmptyResult<DataError.Network> = client.post<UpdatePlaylistReq, Unit>(
        route = EndPoints.UpdatePlaylist.route,
        body = UpdatePlaylistReq(
            songId = songId,
            playlistIdList = mapOf(playlistId to true)
        ),
        gson = gson
    ).asEmptyDataResult()
}