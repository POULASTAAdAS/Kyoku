package com.poulastaa.plugins

import com.poulastaa.domain.repository.ServiceRepository
import com.poulastaa.routes.getCoverImage
import com.poulastaa.routes.setup.getSpotifyPlaylist
import com.poulastaa.routes.setup.requestGenre
import com.poulastaa.routes.setup.storeBDate
import com.poulastaa.routes.setup.storeGenre
import com.poulastaa.routes.unAuthorized
import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    routing {
        val service by inject<ServiceRepository>()

        setUpRoutes(service)
        getCoverImage(service)

        unAuthorized()
    }
}


private fun Routing.setUpRoutes(service: ServiceRepository) {
    getSpotifyPlaylist(service)
    storeBDate(service)
    requestGenre(service)
    storeGenre(service)
}