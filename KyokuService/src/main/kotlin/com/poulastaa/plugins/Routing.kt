package com.poulastaa.plugins

import com.poulastaa.domain.repository.UserRepository
import com.poulastaa.routes.getCoverImage
import com.poulastaa.routes.setup.getSpotifyPlaylist
import com.poulastaa.routes.unAuthorized
import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    routing {
        setUpRoutes()


        unAuthorized()
    }
}


private fun Routing.setUpRoutes() {
    val service by inject<UserRepository>()

    getSpotifyPlaylist(service)
    getCoverImage(service)
}