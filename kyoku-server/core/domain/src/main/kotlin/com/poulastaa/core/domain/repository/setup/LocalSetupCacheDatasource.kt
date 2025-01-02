package com.poulastaa.core.domain.repository.setup

import com.poulastaa.core.domain.model.DtoSong

interface LocalSetupCacheDatasource {
    fun cacheSongByTitle(list: List<String>): List<DtoSong>
    fun setSongIdByTitle(list: List<DtoSong>)
    fun setSongById(list: List<DtoSong>)
}