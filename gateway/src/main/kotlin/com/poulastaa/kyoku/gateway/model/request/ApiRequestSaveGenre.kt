package com.poulastaa.kyoku.gateway.model.request

import com.poulastaa.kyoku.gateway.utils.UserId

data class ApiRequestSaveGenre(
    val userId: UserId,
    val genreIdList: List<Int>,
)
