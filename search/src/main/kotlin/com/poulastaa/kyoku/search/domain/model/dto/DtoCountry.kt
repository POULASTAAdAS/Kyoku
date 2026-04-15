package com.poulastaa.kyoku.search.domain.model.dto

import com.poulastaa.kyoku.search.utils.Country
import com.poulastaa.kyoku.search.utils.CountryId

data class DtoCountry(
    val id: CountryId = 0,
    val name: Country = "",
    val code: String = "",
)
