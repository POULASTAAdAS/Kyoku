package com.poulastaa.core.domain.repository.view

import com.poulastaa.core.domain.model.DtoDetailedPrevSong
import com.poulastaa.core.domain.model.DtoPrevArtist
import com.poulastaa.core.domain.repository.ArtistId
import com.poulastaa.core.domain.repository.SongId

interface LocalViewCacheDatasource {
    fun cacheArtistById(artistId: ArtistId): DtoPrevArtist?
    fun setArtistById(artist: DtoPrevArtist)

    fun cacheDetailedPrevSongById(list: List<SongId>): List<DtoDetailedPrevSong>
    fun setDetailedPrevSongById(songs: List<DtoDetailedPrevSong>)
}