package com.poulastaa.core.domain.repository.suggestion

import com.poulastaa.core.domain.model.DtoPrevSong
import com.poulastaa.core.domain.repository.SongId

interface LocalSuggestionCacheDatasource {
    fun setPrevSongById(song: DtoPrevSong)
    fun setPrevSongById(list: List<DtoPrevSong>)
    fun cachePrevSongById(list: List<SongId>): List<DtoPrevSong>
    fun cachePrevSongById(songId: SongId): DtoPrevSong?
}