package com.poulastaa.kyoku.playlist.domain.model

import com.poulastaa.kyoku.playlist.utils.CountryId

data class DtoCountry(
    val id: CountryId = 0,
    val country: String,
    val code: String,
)
