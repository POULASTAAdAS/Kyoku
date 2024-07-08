package com.poulastaa.core.domain

sealed class EndPoints(val route: String) {
    data object Auth : EndPoints(route = "/api/auth")

    data object SignUpEmailVerificationCheck :
        EndPoints(route = "/api/auth/signUpEmailVerificationCheck")

    data object LogInEmailVerificationCheck :
        EndPoints(route = "/api/auth/logInEmailVerificationCheck")

    data object ForgotPassword : EndPoints(route = "/api/auth/forgotPassword")

    data object RefreshToken : EndPoints(route = "/api/auth/refreshToken")

    data object GetSpotifyPlaylistSong : EndPoints(route = "/api/authorised/spotifyPlaylist")

    data object StoreBDate : EndPoints(route = "/api/authorised/storeBDate")

    data object SuggestGenre : EndPoints(route = "/api/authorised/suggestGenre")
    data object StoreGenre : EndPoints(route = "/api/authorised/storeGenre")

    data object SuggestArtist : EndPoints(route = "/api/authorised/suggestArtist")
    data object GetArtistImage : EndPoints(route = "/api/authorised/getArtistImage")
    data object StoreArtist : EndPoints(route = "/api/authorised/storeArtist")


}