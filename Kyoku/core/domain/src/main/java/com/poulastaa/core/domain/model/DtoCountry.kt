package com.poulastaa.core.domain.model

typealias CountryId = Int

data class DtoCountry(
    val id: CountryId = -1,
    val name: String = "",
)
