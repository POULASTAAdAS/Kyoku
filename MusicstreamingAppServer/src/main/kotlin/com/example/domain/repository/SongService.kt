package com.example.domain.repository

interface SongService {
    suspend fun readSong():String
}