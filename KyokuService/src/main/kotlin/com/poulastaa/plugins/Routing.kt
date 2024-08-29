package com.poulastaa.plugins

import com.poulastaa.domain.repository.ServiceRepository
import com.poulastaa.routes.*
import com.poulastaa.routes.setup.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import java.io.File

fun Application.configureRouting() {
    routing {
        val service by inject<ServiceRepository>()

        setUpRoutes(service)
        common(service)
        home(service)

        staticFiles(
            remotePath = ".well-known",
            dir = File("certs")
        )

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

    createPlaylist(service)

    pinData(service)
    unPinData(service)

    deleteSavedData(service)

    getListOfData(service)

    viewArtist(service)

    getArtist(service)
    getArtistSongPagerData(service)
    getArtistAlbumPagerData(service)
}

private fun Routing.setUpRoutes(service: ServiceRepository) {
    getSpotifyPlaylist(service)
    storeBDate(service)
    requestGenre(service)
    storeGenre(service)
    requestArtist(service)
    storeArtist(service)
}