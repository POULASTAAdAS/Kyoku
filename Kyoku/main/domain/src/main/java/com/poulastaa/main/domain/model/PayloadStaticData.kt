package com.poulastaa.main.domain.model

import com.poulastaa.core.domain.model.DtoPrevAlbum
import com.poulastaa.core.domain.model.DtoPrevArtist
import com.poulastaa.core.domain.model.DtoPrevSong

data class PayloadStaticData(
    val popularSongMix: List<DtoPrevSong> = emptyList(),
    val popularSongFromYourTime: List<DtoPrevSong> = emptyList(),
    val favouriteArtistMix: List<DtoPrevSong> = emptyList(),
    val dayTypeSong: List<DtoPrevSong> = emptyList(),

    val popularAlbum: List<DtoPrevAlbum> = emptyList(),
    val suggestedArtist: List<DtoPrevArtist> = emptyList(),
    val popularArtistSong: List<DtoSuggestedArtistSong> = emptyList(),
)