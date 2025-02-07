package com.poulastaa.core.domain.repository.setup

import com.poulastaa.core.domain.model.*
import com.poulastaa.core.domain.repository.ArtistId
import com.poulastaa.core.domain.repository.GenreId
import com.poulastaa.core.domain.repository.auth.Email
import java.time.LocalDate

interface LocalSetupCacheDatasource {
    fun cacheSongByTitle(list: List<String>): List<DtoSong>
    fun setSongIdByTitle(list: List<DtoSong>)
    fun setSongById(list: List<DtoSong>)

    fun setUserByEmail(key: Email, type: UserType, value: DtoDBUser)

    fun cacheGenreById(list: List<GenreId>): List<DtoGenre>
    fun cacheGenreByName(query: String, size: Int): List<DtoGenre>

    fun setGenreById(list: List<DtoGenre>)
    fun setGenreIdByName(list: Map<String, GenreId>)

    fun cachePrevGenreByUserId(userId: Long): List<DtoPrevGenre>
    fun setPrevGenreByUserId(userId: Long, data: List<DtoPrevGenre>)

    fun cachePrevArtistById(list: List<ArtistId>): List<DtoPrevArtist>
    fun cachePrevArtistByName(query: String, size: Int): List<DtoPrevArtist>

    fun setPrevArtistById(list: List<DtoPrevArtist>)
    fun setPrevArtistIdByName(list: Map<String, ArtistId>)

    fun cacheArtistById(list: List<ArtistId>): List<DtoArtist>
}