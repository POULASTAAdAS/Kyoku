package com.poulastaa.core.domain.model

sealed class Endpoints(val route: String) {
    data object Auth : Endpoints(route = "/api/v1/auth")

    data object VerifyEmail : Endpoints(route = "/api/v1/auth/verifyEmail")
    data object GetJWTToken : Endpoints(route = "/api/v1/auth/getJWTToken")

    data object ForgotPassword : Endpoints(route = "/api/v1/auth/forgotPassword")
    data object ResetPassword : Endpoints(route = "/api/v1/auth/resetPassword")
    data object SubmitNewPassword : Endpoints(route = "/api/v1/auth/submitNewPassword")

    data object ImportSpotifyPlaylist : Endpoints(route = "/api/v1/user/importSpotifyPlaylist")

    sealed class Poster {
        data object SongPoster : Endpoints(route = "/api/v1/poster/song")
        data object GenrePoster : Endpoints(route = "/api/v1/poster/genre")
        data object ArtistPoster : Endpoints(route = "/api/v1/poster/artist")
    }

    data object SetBDate : Endpoints(route = "/api/v1/user/setBDate")

    data object PicGenre : Endpoints(route = "/api/v1/user/picGenre")

    data object UnAuthorized : Endpoints(route = "/api/v1/unauthorized")
}