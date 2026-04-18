package com.poulastaa.kyoku.search.domain.model.dto

import com.poulastaa.kyoku.search.utils.CountryId
import com.poulastaa.kyoku.search.utils.CountryName

data class DtoCountry(
    val id: CountryId = 0,
    val name: CountryName = "",
    val code: String = "",
)
