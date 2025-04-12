package com.poulastaa.search.model

internal data class PagingParams(
    val size: Int,
    val page: Int,
    val query: String?,
)
