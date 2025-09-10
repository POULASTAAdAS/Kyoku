package com.poulastaa.kyoku.file.model

object Endpoints {
    private const val API = "api"
    private const val VERSION: String = "v1"
    private const val TYPE = "file"

    const val BASE_URL = "/$API/$VERSION/$TYPE"

    const val GET_STATIC_CONTENT = "$BASE_URL/static"
}