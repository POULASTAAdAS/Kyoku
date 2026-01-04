package com.poulastaa.kyoku.playlist.service

import com.poulastaa.kyoku.playlist.domain.model.DtoSong
import com.poulastaa.kyoku.playlist.utils.SongTitle
import org.springframework.stereotype.Service

@Service
class RedisCacheService {
    fun cacheSongByTitle(titles: List<SongTitle>): Map<SongTitle, DtoSong> = TODO("return cached song")
    fun setSongByTitle(songs: List<DtoSong>): Unit = TODO("return cached song")
    fun setSongById(songs: List<DtoSong>): Unit = TODO("return cached song")
}