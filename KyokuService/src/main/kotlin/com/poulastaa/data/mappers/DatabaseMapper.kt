package com.poulastaa.data.mappers

import com.poulastaa.data.dao.ArtistDao
import com.poulastaa.domain.model.ResultArtist

fun ArtistDao.toArtistResult() = ResultArtist(
    id = this.id.value,
    name = this.name,
    profilePic = this.constructProfilePic() ?: ""
)

