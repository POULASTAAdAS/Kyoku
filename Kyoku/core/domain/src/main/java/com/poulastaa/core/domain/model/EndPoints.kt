package com.poulastaa.core.domain.model

sealed class EndPoints(val route: String) {
    data object Auth : EndPoints(route = "/api/v1/auth")

    data object GetJWTToken : EndPoints(route = "/api/v1/auth/getJWTToken")
    data object GenerateRefreshToken : EndPoints(route = "/api/v1/auth/generateRefreshToken")

    data object ForgotPassword : EndPoints(route = "/api/v1/auth/forgotPassword")

    data object ImportSpotifyPlaylist : EndPoints(route = "/api/v1/user/importSpotifyPlaylist")

    data object SetBDate : EndPoints(route = "/api/v1/user/setBDate")

    data object SuggestGenre : EndPoints(route = "/api/v1/user/suggestGenre")
    data object UPSERTGenre : EndPoints(route = "/api/v1/user/upsertGenre")

    data object SuggestArtist : EndPoints(route = "/api/v1/user/suggestArtist")
    data object UPSERTArtist : EndPoints(route = "/api/v1/user/upsertArtist")
}