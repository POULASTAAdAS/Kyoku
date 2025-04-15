package com.poulastaa.core.domain.repository.item

import com.poulastaa.core.domain.model.DtoArtist
import com.poulastaa.core.domain.model.DtoDBUser
import com.poulastaa.core.domain.model.DtoPlaylist
import com.poulastaa.core.domain.model.UserType
import com.poulastaa.core.domain.repository.ArtistId
import com.poulastaa.core.domain.repository.UserId

interface LocalItemDatasource {
    suspend fun getUserByEmail(email: String, userType: UserType): DtoDBUser?
    suspend fun getArtistById(artistId: ArtistId): DtoArtist?
    suspend fun createPlaylist(playlistName: String, userId: UserId): DtoPlaylist?
}