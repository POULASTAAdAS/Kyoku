package com.poulastaa.kyoku.search.database.repository

import com.poulastaa.kyoku.search.domain.model.dto.DtoArtist
import com.poulastaa.kyoku.search.utils.CountryCode
import org.springframework.stereotype.Service

@Service
interface ContentESRepository {
    fun getMostPopularArtistByCountry(size: Int, country: CountryCode): List<DtoArtist>
}