package com.poulastaa.add.network.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.google.gson.Gson
import com.poulastaa.add.domain.model.DtoAddSongToPlaylistArtistSearchFilterType
import com.poulastaa.add.domain.model.DtoAddSongToPlaylistItem
import com.poulastaa.add.domain.model.DtoAddSongToPlaylistPageItem
import com.poulastaa.add.domain.model.DtoAddSongToPlaylistSearchFilterType
import com.poulastaa.add.domain.model.DtoAddToPlaylistItemType
import com.poulastaa.add.domain.repository.RemoteAddSongToPlaylistDatasource
import com.poulastaa.add.network.mapper.toDtoAddSongToPlaylistPageItem
import com.poulastaa.add.network.model.AddSongToPlaylistPageItemResponse
import com.poulastaa.add.network.model.AddSongToPlaylistRequest
import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.Result
import com.poulastaa.core.domain.map
import com.poulastaa.core.domain.model.AlbumId
import com.poulastaa.core.domain.model.ArtistId
import com.poulastaa.core.domain.model.DtoPrevArtist
import com.poulastaa.core.domain.model.DtoSong
import com.poulastaa.core.domain.model.EndPoints
import com.poulastaa.core.domain.model.PlaylistId
import com.poulastaa.core.domain.model.SongId
import com.poulastaa.core.domain.model.ViewType
import com.poulastaa.core.network.ApiMethodType
import com.poulastaa.core.network.ReqParam
import com.poulastaa.core.network.mapper.toDtoPrevArtist
import com.poulastaa.core.network.mapper.toDtoSong
import com.poulastaa.core.network.model.ResponseArtist
import com.poulastaa.core.network.model.ResponseSong
import com.poulastaa.core.network.req
import com.poulastaa.view.domain.repository.RemoteViewOtherDatasource
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.InternalSerializationApi
import okhttp3.OkHttpClient
import javax.inject.Inject

@OptIn(InternalSerializationApi::class)
internal class OkHttpAddSongToPlaylistDatasource @Inject constructor(
    private val client: OkHttpClient,
    private val gson: Gson,
    private val playlistPager: AddSongToPlaylistPagingSource,
    private val artistPager: AddSongToPlaylistArtistPagingSource,
    private val view: RemoteViewOtherDatasource,
) : RemoteAddSongToPlaylistDatasource {
    override suspend fun loadStaticData(): Result<List<DtoAddSongToPlaylistPageItem>, DataError.Network> =
        client.req<Unit, List<AddSongToPlaylistPageItemResponse>>(
            route = EndPoints.Add.Playlist.CreatePlaylistStaticData.route,
            method = ApiMethodType.GET,
            gson = gson
        ).map { list ->
            list.map { it.toDtoAddSongToPlaylistPageItem() }
        }

    override fun search(
        query: String,
        filterType: DtoAddSongToPlaylistSearchFilterType,
    ): Flow<PagingData<DtoAddSongToPlaylistItem>> {
        playlistPager.init(query, filterType)

        return Pager(
            config = PagingConfig(pageSize = 10, initialLoadSize = 15),
            initialKey = 1,
            pagingSourceFactory = { playlistPager }
        ).flow
    }

    override suspend fun saveSong(
        playlistId: PlaylistId,
        songId: SongId,
    ): Result<DtoSong, DataError.Network> = client.req<AddSongToPlaylistRequest, ResponseSong>(
        route = EndPoints.Add.AddSong.route,
        method = ApiMethodType.POST,
        body = AddSongToPlaylistRequest(
            playlistId = playlistId,
            songId = songId
        ),
        gson = gson
    ).map { it.toDtoSong() }

    override suspend fun loadAlbum(
        albumId: AlbumId,
    ): Result<Pair<String, List<DtoAddSongToPlaylistItem>>, DataError.Network> = view.getViewAlbum(
        type = ViewType.ALBUM,
        albumId = albumId
    ).map {
        it.heading.name to it.listOfSongs.map {
            DtoAddSongToPlaylistItem(
                id = it.id,
                title = it.title,
                poster = listOf(it.poster ?: ""),
                artist = it.artists,
                type = DtoAddToPlaylistItemType.SONG,
            )
        }
    }

    override suspend fun loadArtist(artistId: ArtistId): Result<DtoPrevArtist, DataError.Network> =
        client.req<Unit, ResponseArtist>(
            route = EndPoints.Artist.GetArtist.route,
            method = ApiMethodType.GET,
            params = listOf(
                ReqParam(
                    key = "artistId",
                    value = artistId.toString()
                )
            ),
            gson = gson
        ).map { it.toDtoPrevArtist() }

    override suspend fun loadArtistSearch(
        artistId: ArtistId,
        query: String,
        filterType: DtoAddSongToPlaylistArtistSearchFilterType,
    ): Flow<PagingData<DtoAddSongToPlaylistItem>> {
        return Pager(
            config = PagingConfig(pageSize = 10, initialLoadSize = 15),
            initialKey = 1,
            pagingSourceFactory = {
                artistPager.init(
                    query = query,
                    artistId = artistId,
                    filterType = filterType
                )
            }
        ).flow
    }
}