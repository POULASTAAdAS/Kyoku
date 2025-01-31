package com.poulastaa.suggestion.domain.model

import com.poulastaa.core.domain.model.DtoArtist
import com.poulastaa.core.domain.model.DtoFullAlbum
import com.poulastaa.core.domain.model.DtoFullPlaylist

data class DtoHome(
    val refresh: DtoRefresh = DtoRefresh(),

    val playlist: List<DtoFullPlaylist> = emptyList(),
    val album: List<DtoFullAlbum> = emptyList(),
    val artist: List<DtoArtist> = emptyList(),
)
