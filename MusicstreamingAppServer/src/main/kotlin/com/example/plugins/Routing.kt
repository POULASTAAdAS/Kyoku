package com.example.plugins

import com.example.domain.repository.user.EmailAuthUserRepository
import com.example.domain.repository.user.GoogleAuthUserRepository
import com.example.routes.auth.*
import com.example.routes.getSpotifyPlaylist
import com.example.routes.getUserProfilePic
import com.example.routes.unauthorised
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.get
import java.io.File

fun Application.configureRouting() {
    val emailAuthUser: EmailAuthUserRepository = get()
    val googleAuthUser: GoogleAuthUserRepository = get()

    routing {
        createAuthRoute(
            emailAuthUser = emailAuthUser,
            googleAuthUser = googleAuthUser
        )
        verifyEmail(emailAuthUser = emailAuthUser)
        emailVerificationCheck(emailAuthUser = emailAuthUser)
        forgotPassword(emailAuthUser = emailAuthUser)
        resetPassword(emailAuthUser = emailAuthUser)

        getUserProfilePic(emailAuthUser = emailAuthUser)

        getSpotifyPlaylist()

        unauthorised()
        static(".well-known") {
            staticRootFolder = File("certs")
            file("jwks.json")
        }
    }
}