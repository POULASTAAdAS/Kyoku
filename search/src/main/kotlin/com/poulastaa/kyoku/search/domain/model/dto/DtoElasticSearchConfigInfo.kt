package com.poulastaa.kyoku.search.domain.model.dto

data class DtoElasticSearchConfigInfo(
    val host: String,
    val port: Int,
    val username: String,
    val password: String,
    val schema: String,
)
