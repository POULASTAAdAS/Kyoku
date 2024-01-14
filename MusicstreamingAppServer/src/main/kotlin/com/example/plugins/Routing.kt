package com.example.plugins

import com.example.domain.repository.UserServiceRepository
import com.example.domain.repository.song_db.SongRepository
import com.example.routes.auth.*
import com.example.routes.common.addSessionInterceptor
import com.example.routes.getCoverPhoto
import com.example.routes.getSpotifyPlaylist
import com.example.routes.getUserProfilePic
import com.example.routes.unauthorised
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import java.io.File

fun Application.configureRouting() {
    val userService: UserServiceRepository by inject()
    val songs: SongRepository by inject()

    routing {
        addSessionInterceptor()

        authRoute(userService = userService)
        verifyEmail(userService = userService)
        emailVerificationCheck(userService = userService)

        forgotPassword(userService = userService)
        resetPassword(userService = userService)

        getUserProfilePic(userService = userService)

        getSpotifyPlaylist(songRepository = songs)
        getCoverPhoto(songRepository = songs)

        refreshToken(userService = userService)

        unauthorised()

        static(".well-known") {
            staticRootFolder = File("certs")
            file("jwks.json")
        }
    }
}