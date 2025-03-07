package com.poulastaa.core.domain.repository.view

import com.poulastaa.core.domain.model.DtoPrevArtist
import com.poulastaa.core.domain.model.DtoPrevSong
import com.poulastaa.core.domain.repository.ArtistId
import com.poulastaa.core.domain.repository.SongId

interface LocalViewCacheDatasource {
    fun cacheArtistById(artistId: ArtistId): DtoPrevArtist?
    fun cachePrevSongById(list: List<SongId>): List<DtoPrevSong>
    fun setPrevSongById(songs: List<DtoPrevSong>)
}