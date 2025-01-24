package com.poulastaa.app.plugins

import com.poulastaa.auth.domain.repository.AuthRepository
import com.poulastaa.auth.network.routes.*
import com.poulastaa.user.domain.repository.SetupRepository
import com.poulastaa.user.network.routes.getArtistPoster
import com.poulastaa.user.network.routes.getGenrePoster
import com.poulastaa.user.network.routes.getSongPoster
import com.poulastaa.user.network.routes.setup.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import java.io.File

fun Application.configureRouting() {
    val authRepository: AuthRepository by inject()
    val setUpRepository: SetupRepository by inject()

    routing {
        authen(authRepository)
        setup(setUpRepository)

        staticFiles(
            remotePath = ".well-known",
            dir = File("assets/certs")
        )

        staticFiles(
            remotePath = "images",
            dir = File("assets/images")
        )
    }
}

private fun Routing.authen(repo: AuthRepository) {
    auth(repo)
    verifyEmail(repo)
    getJWTToken(repo)
    forgotPassword(repo)
    changePassword(repo)
    resetPassword(repo)

    unAuthorized()
}

private fun Routing.setup(repo: SetupRepository) {
    importSpotifyPlaylist(
        clientId = this.environment.config.property("spotify.clientId").getString(),
        clientSecret = this.environment.config.property("spotify.clientSecret").getString(),
        repo = repo
    )
    getSongPoster()

    setBDate(repo)

    suggestGenre(repo)
    getGenrePoster()
    upsertGenre(repo)

    suggestArtist(repo)
    getArtistPoster()
}