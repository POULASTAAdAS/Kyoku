package com.poulastaa.add.network.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.google.gson.Gson
import com.poulastaa.add.domain.model.DtoAddSongToPlaylistArtistSearchFilterType
import com.poulastaa.add.domain.model.DtoAddSongToPlaylistItem
import com.poulastaa.add.domain.model.DtoAddToPlaylistItemType
import com.poulastaa.add.network.mapper.toDtoAddSongToPlaylistItem
import com.poulastaa.add.network.model.ResponseExploreItem
import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.NoInternetException
import com.poulastaa.core.domain.Result
import com.poulastaa.core.domain.UnknownRemoteException
import com.poulastaa.core.domain.map
import com.poulastaa.core.domain.model.ArtistId
import com.poulastaa.core.domain.model.EndPoints
import com.poulastaa.core.network.ApiMethodType
import com.poulastaa.core.network.ReqParam
import com.poulastaa.core.network.req
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.serialization.InternalSerializationApi
import okhttp3.OkHttpClient
import javax.inject.Inject
import kotlin.random.Random

@OptIn(InternalSerializationApi::class)
internal class AddSongToPlaylistArtistPagingSource @Inject constructor(
    private val client: OkHttpClient,
    private val gson: Gson,
) : PagingSource<Int, DtoAddSongToPlaylistItem>() {
    private var query = ""
    private var artistId: ArtistId = -1
    private var filterType = DtoAddSongToPlaylistArtistSearchFilterType.ALL

    override fun getRefreshKey(state: PagingState<Int, DtoAddSongToPlaylistItem>) =
        state.anchorPosition

    fun init(
        query: String,
        artistId: ArtistId,
        filterType: DtoAddSongToPlaylistArtistSearchFilterType,
    ): AddSongToPlaylistArtistPagingSource {
        this.query = query
        this.artistId = artistId
        this.filterType = filterType

        return this
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, DtoAddSongToPlaylistItem> {
        if (artistId == -1L) return LoadResult.Error(UnknownRemoteException)

        val page = params.key ?: 1

        return when (filterType) {
            DtoAddSongToPlaylistArtistSearchFilterType.ALL -> {
                val loadSize = params.loadSize / 2

                coroutineScope {
                    val albumDef = async {
                        getPagingData(
                            loadSize = loadSize,
                            artistId = artistId,
                            page = page,
                            route = EndPoints.Artist.GetArtistPagingAlbums,
                            type = DtoAddToPlaylistItemType.ALBUM
                        )
                    }

                    val songDef = async {
                        getPagingData(
                            loadSize = loadSize,
                            artistId = artistId,
                            page = page,
                            route = EndPoints.Artist.GetArtistPagingSongs,
                            type = DtoAddToPlaylistItemType.SONG
                        )
                    }

                    val album = albumDef.await()
                    val song = songDef.await()

                    when {
                        album is Result.Success && song is Result.Success -> LoadResult.Page(
                            data = (album.data + song.data).shuffled(Random),
                            prevKey = if (page == 1) null else page.minus(1),
                            nextKey = if (album.data.isEmpty() && song.data.isEmpty()) null else page.plus(
                                1
                            )
                        )

                        album is Result.Success && song is Result.Error -> LoadResult.Page(
                            data = album.data,
                            prevKey = if (page == 1) null else page.minus(1),
                            nextKey = if (album.data.isEmpty()) null else page.plus(1)
                        )

                        song is Result.Success && album is Result.Error -> LoadResult.Page(
                            data = song.data,
                            prevKey = if (page == 1) null else page.minus(1),
                            nextKey = if (song.data.isEmpty()) null else page.plus(1)
                        )

                        song is Result.Error && album is Result.Error -> when (song.error) {
                            DataError.Network.NO_INTERNET -> LoadResult.Error(NoInternetException)
                            else -> LoadResult.Error(UnknownRemoteException)
                        }

                        else -> LoadResult.Error(UnknownRemoteException)
                    }
                }
            }

            DtoAddSongToPlaylistArtistSearchFilterType.ALBUM -> {
                val result = getPagingData(
                    loadSize = params.loadSize,
                    artistId = artistId,
                    page = page,
                    route = EndPoints.Artist.GetArtistPagingAlbums,
                    type = DtoAddToPlaylistItemType.ALBUM
                )

                when (result) {
                    is Result.Error -> when (result.error) {
                        DataError.Network.NO_INTERNET -> LoadResult.Error(NoInternetException)
                        else -> LoadResult.Error(UnknownRemoteException)
                    }

                    is Result.Success -> LoadResult.Page(
                        data = result.data,
                        prevKey = if (page == 1) null else page.minus(1),
                        nextKey = if (result.data.isEmpty()) null else page.plus(1)
                    )
                }
            }

            DtoAddSongToPlaylistArtistSearchFilterType.SONG -> {
                val result = getPagingData(
                    loadSize = params.loadSize,
                    artistId = artistId,
                    page = page,
                    route = EndPoints.Artist.GetArtistPagingSongs,
                    type = DtoAddToPlaylistItemType.SONG
                )

                when (result) {
                    is Result.Error -> when (result.error) {
                        DataError.Network.NO_INTERNET -> LoadResult.Error(NoInternetException)
                        else -> LoadResult.Error(UnknownRemoteException)
                    }

                    is Result.Success -> LoadResult.Page(
                        data = result.data,
                        prevKey = if (page == 1) null else page.minus(1),
                        nextKey = if (result.data.isEmpty()) null else page.plus(1)
                    )
                }
            }
        }
    }

    private suspend fun getPagingData(
        loadSize: Int,
        artistId: ArtistId,
        page: Int,
        route: EndPoints,
        type: DtoAddToPlaylistItemType,
    ) = client.req<Unit, List<ResponseExploreItem>>(
        route = route.route,
        method = ApiMethodType.GET,
        params = listOf(
            ReqParam(
                key = "page",
                value = page.toString()
            ),
            ReqParam(
                key = "size",
                value = loadSize.toString()
            ),
            ReqParam(
                key = "artistId",
                value = artistId.toString()
            ),
            ReqParam(
                key = "query",
                value = query.trim()
            )
        ),
        gson = gson
    ).map { list ->
        list.map {
            it.toDtoAddSongToPlaylistItem(type)
        }
    }
}