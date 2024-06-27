package com.poulastaa.setup.network

import okhttp3.OkHttpClient
import javax.inject.Inject

class SpotifyRemoteDataSource @Inject constructor(
    private val client: OkHttpClient,
) {

}