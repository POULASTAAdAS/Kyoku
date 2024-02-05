package com.poulastaa.plugins

import com.poulastaa.domain.repository.song.SongRepository
import com.poulastaa.routes.getSongCover
import com.poulastaa.routes.unauthorized
import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRouting(){
    val songs: SongRepository by inject()

    routing {
        getSongCover(songs)

        unauthorized()
    }
}