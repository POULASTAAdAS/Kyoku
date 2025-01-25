package com.poulastaa.core.domain.repository.setup

import com.poulastaa.core.domain.model.DtoArtist
import com.poulastaa.core.domain.model.DtoGenre
import com.poulastaa.core.domain.model.DtoPrevArtist
import com.poulastaa.core.domain.model.DtoSong
import com.poulastaa.core.domain.repository.ArtistId
import com.poulastaa.core.domain.repository.GenreId

interface LocalSetupCacheDatasource {
    fun cacheSongByTitle(list: List<String>): List<DtoSong>
    fun setSongIdByTitle(list: List<DtoSong>)
    fun setSongById(list: List<DtoSong>)

    fun cacheGenreById(id: List<GenreId>): List<DtoGenre>
    fun cacheGenreByName(query: String, size: Int): List<DtoGenre>

    fun setGenreById(list: List<DtoGenre>)
    fun setGenreIdByName(list: Map<String, GenreId>)

    fun cachePrevArtistById(id: List<ArtistId>): List<DtoPrevArtist>
    fun cachePrevArtistByName(query: String, size: Int): List<DtoPrevArtist>

    fun setPrevArtistById(list: List<DtoPrevArtist>)
    fun setPrevArtistIdByName(list: Map<String, ArtistId>)

    fun cacheArtistById(list: List<ArtistId>): List<DtoArtist>
}