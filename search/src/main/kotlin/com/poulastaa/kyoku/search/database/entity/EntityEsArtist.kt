package com.poulastaa.kyoku.search.database.entity

import com.poulastaa.kyoku.search.domain.model.dto.DtoArtist
import com.poulastaa.kyoku.search.utils.ArtistId
import com.poulastaa.kyoku.search.utils.ArtistTitle
import com.poulastaa.kyoku.search.utils.CountryCode
import com.poulastaa.kyoku.search.utils.CountryName

data class EntityEsArtist(
    val id: ArtistId,
    val name: ArtistTitle,
    val countryCode: CountryCode,
    val coverImage: String?,
    val countryName: CountryName,
    val followers: Long,
) {
    fun toDtoArtist() = DtoArtist(
        id = id,
        name = name,
        cover = coverImage,
        popularity = followers
    )
}
