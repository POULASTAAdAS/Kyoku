package com.poulastaa.network.pager_source

//class ViewArtistPagerSource @Inject constructor(
//    private val client: OkHttpClient,
//    private val gson: Gson,
//) : PagingSource<Int, ViewArtistSongRes>() {
//    private var artistId: Long? = null
//    private var ds: DataStoreRepository? = null
//
//    fun init(id: Long, ds: DataStoreRepository) {
//        this.artistId = id
//        this.ds = ds
//    }
//
//    override fun getRefreshKey(state: PagingState<Int, ViewArtistSongRes>): Int? =
//        state.anchorPosition
//
//    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ViewArtistSongRes> {
//        val header =
//            ds?.readTokenOrCookie()?.first() ?: return LoadResult.Error(CustomNullPointerException)
//        val page = params.key ?: 1
//        val pageSize = params.loadSize
//
//
//
//    }
//
//}