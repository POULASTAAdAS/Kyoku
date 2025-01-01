package com.poulastaa.core.domain.repository.setup

import com.poulastaa.core.domain.model.DtoSong

interface LocalSetupCacheDatasource {
    fun getSongByTitle(list: List<String>): List<DtoSong>
}