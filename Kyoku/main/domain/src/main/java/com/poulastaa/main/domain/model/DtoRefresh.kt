package com.poulastaa.main.domain.model

import com.poulastaa.core.domain.model.DtoAlbum
import com.poulastaa.core.domain.model.DtoPrevArtist
import com.poulastaa.core.domain.model.DtoPrevSong

data class DtoRefresh(
    val prevPopularSongMix: List<DtoPrevSong> = emptyList(),
    val prevPopularArtistMix: List<DtoPrevSong> = emptyList(),
    val prevOldGem: List<DtoPrevSong> = emptyList(),

    val suggestedArtist: List<DtoPrevArtist> = emptyList(),
    val suggestedAlbum: List<DtoAlbum> = emptyList(),
    val suggestedArtistSong: List<DtoSuggestedArtistSong> = emptyList(),
)
