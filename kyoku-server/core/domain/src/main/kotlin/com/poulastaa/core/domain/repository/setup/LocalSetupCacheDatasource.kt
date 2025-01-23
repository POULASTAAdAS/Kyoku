package com.poulastaa.core.domain.repository.setup

import com.poulastaa.core.domain.model.DtoGenre
import com.poulastaa.core.domain.model.DtoSong
import com.poulastaa.core.domain.repository.GenreId

interface LocalSetupCacheDatasource {
    fun cacheSongByTitle(list: List<String>): List<DtoSong>
    fun setSongIdByTitle(list: List<DtoSong>)
    fun setSongById(list: List<DtoSong>)
    fun cacheGenre(id: List<GenreId>): List<DtoGenre>
    fun cacheGenreByQuery(query: String, size: Int): List<DtoGenre>
    fun setGenreById(list: List<DtoGenre>)
    fun setGenreIdByName(list: Map<String, GenreId>)
}