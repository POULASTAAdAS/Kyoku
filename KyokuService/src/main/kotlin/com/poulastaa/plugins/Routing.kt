package com.poulastaa.plugins

import com.poulastaa.domain.repository.ServiceRepository
import com.poulastaa.routes.*
import com.poulastaa.routes.setup.*
import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    routing {
        val service by inject<ServiceRepository>()

        setUpRoutes(service)
        common(service)
        home(service)

        unAuthorized()
    }
}

private fun Routing.home(service: ServiceRepository) {
    getLogInData(service)
    homeReq(service)
}


private fun Routing.common(service: ServiceRepository) {
    getCoverImage()
    getArtistImage()

    addToFavourite(service)
    removeFromFavourite(service)

    followArtist(service)
    unFollowArtist(service)

    addAlbum(service)
    removeAlbum(service)

    savePlaylist(service)

    getSong(service)
    updatePlaylist(service)
    updateFavourite(service)
}

private fun Routing.setUpRoutes(service: ServiceRepository) {
    getSpotifyPlaylist(service)
    storeBDate(service)
    requestGenre(service)
    storeGenre(service)
    requestArtist(service)
    storeArtist(service)
}