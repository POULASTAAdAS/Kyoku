package com.poulastaa.search.model

data class PagingParams(
    val size: Int,
    val page: Int,
    val query: String?,
)
