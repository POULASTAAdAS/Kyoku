package com.poulastaa.suggestion.domain.model

import com.poulastaa.core.domain.model.DtoPrevAlbum
import com.poulastaa.core.domain.model.DtoPrevArtist
import com.poulastaa.core.domain.model.DtoPrevSong
import com.poulastaa.core.domain.model.DtoSuggestedArtistSong

data class DtoRefresh(
    val prevPopularSongMix: List<DtoPrevSong> = emptyList(),
    val prevPopularArtistMix: List<DtoPrevSong> = emptyList(),
    val prevOldGem: List<DtoPrevSong> = emptyList(),

    val suggestedArtist: List<DtoPrevArtist> = emptyList(),
    val suggestedAlbum: List<DtoPrevAlbum> = emptyList(),
    val suggestedArtistSong: List<DtoSuggestedArtistSong> = emptyList(),
)
