package com.poulastaa.user.network.routes

import com.poulastaa.core.domain.model.EndPoints
import com.poulastaa.core.domain.utils.Constants.SECURITY_LIST
import com.poulastaa.user.network.routes.setup.utils.picRandomPoster
import io.ktor.server.auth.*
import io.ktor.server.routing.*

fun Route.getArtistPoster() {
    authenticate(configurations = SECURITY_LIST) {
        route(EndPoints.Poster.ArtistPoster.route) {
            picRandomPoster()
        }
    }
}

