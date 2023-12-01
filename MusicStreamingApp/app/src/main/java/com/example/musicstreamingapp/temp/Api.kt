package com.example.musicstreamingapp.temp

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET

interface Api {
    @GET("/song")
    fun getSong(): Call<ResponseBody>
}