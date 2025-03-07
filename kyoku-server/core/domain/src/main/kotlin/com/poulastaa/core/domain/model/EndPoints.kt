package com.poulastaa.core.domain.model

sealed class EndPoints(val route: String) {
    data object Auth : EndPoints(route = "/api/v1/auth")
    data object GetPasskey : EndPoints(route = "/api/v1/auth/getPasskeyRequest")
    data object CreatePasskeyUser : EndPoints(route = "/api/v1/auth/createPasskeyUser")
    data object GetPasskeyUser : EndPoints(route = "/api/v1/auth/getPasskeyUser")

    data object VerifyEmail : EndPoints(route = "/api/v1/auth/verifyEmail")
    data object GetJWTToken : EndPoints(route = "/api/v1/auth/getJWTToken")
    data object RefreshToken : EndPoints(route = "/api/v1/auth/refreshToken")

    data object ForgotPassword : EndPoints(route = "/api/v1/auth/forgotPassword")
    data object ResetPassword : EndPoints(route = "/api/v1/auth/resetPassword")
    data object SubmitNewPassword : EndPoints(route = "/api/v1/auth/submitNewPassword")

    data object ImportSpotifyPlaylist : EndPoints(route = "/api/v1/user/importSpotifyPlaylist")

    sealed class Poster {
        data object SongPoster : EndPoints(route = "/api/v1/poster/song")
        data object GenrePoster : EndPoints(route = "/api/v1/poster/genre")
        data object ArtistPoster : EndPoints(route = "/api/v1/poster/artist")
    }

    data object SetBDate : EndPoints(route = "/api/v1/user/setBDate")

    data object SuggestGenre : EndPoints(route = "/api/v1/user/suggestGenre")
    data object UPSERTGenre : EndPoints(route = "/api/v1/user/upsertGenre")

    data object SuggestArtist : EndPoints(route = "/api/v1/user/suggestArtist")
    data object UPSERTArtist : EndPoints(route = "/api/v1/user/upsertArtist")

    data object Home : EndPoints(route = "/api/v1/suggestion/home")
    data object RefreshHome : EndPoints(route = "/api/v1/suggestion/refreshHome")

    data object GetBDate : EndPoints(route = "/api/v1/user/getBDate")
    data object UpdateUsername : EndPoints(route = "/api/v1/user/updateUsername")

    data object ViewArtist : EndPoints(route = "/api/v1/view/viewArtist")

    data object UnAuthorized : EndPoints(route = "/api/v1/unauthorized")
}