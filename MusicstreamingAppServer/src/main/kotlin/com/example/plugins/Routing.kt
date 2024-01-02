package com.example.plugins

import com.example.domain.repository.SongService
import com.example.routes.*
import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.get

fun Application.configureRouting() {
    val songService: SongService = get()
    routing {
        getMasterPlaylist(songService = songService)
        getPlaylist()
        getAudio()
    }
}