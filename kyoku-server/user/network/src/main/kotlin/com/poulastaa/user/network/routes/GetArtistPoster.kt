package com.poulastaa.user.network.routes

import com.poulastaa.core.domain.model.EndPoints
import com.poulastaa.core.domain.utils.Constants.CURRENT_PROJECT_FOLDER
import com.poulastaa.core.domain.utils.Constants.POSTER_PARAM
import com.poulastaa.core.domain.utils.Constants.SECURITY_LIST
import com.poulastaa.core.network.getReqUserPayload
import com.poulastaa.user.network.routes.setup.utils.picRandomPoster
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.io.File

fun Route.getArtistPoster() {
    authenticate(configurations = SECURITY_LIST) {
        route(EndPoints.Poster.ArtistPoster.route) {
            picRandomPoster()
        }
    }
}

