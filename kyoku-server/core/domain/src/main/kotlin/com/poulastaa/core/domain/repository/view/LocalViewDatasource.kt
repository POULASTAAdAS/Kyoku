package com.poulastaa.core.domain.repository.view

import com.poulastaa.core.domain.model.DtoDBUser
import com.poulastaa.core.domain.model.DtoDetailedPrevSong
import com.poulastaa.core.domain.model.DtoPrevArtist
import com.poulastaa.core.domain.model.UserType
import com.poulastaa.core.domain.repository.ArtistId

interface LocalViewDatasource {
    suspend fun getUserByEmail(email: String, userType: UserType): DtoDBUser?

    suspend fun getArtist(artistId: ArtistId): DtoPrevArtist?
    suspend fun getArtistMostPopularSongs(artistId: ArtistId): List<DtoDetailedPrevSong>
}