package com.poulastaa.kyoku

sealed class Screens(val route: String) {
    companion object {
        const val AUTH_ROUTE = "auth"
        const val START_UP_ROUTE = "startup"
        const val APP_ROUTE = "app"
    }

    data object Intro : Screens("/intro")
    data object EmailLogIn : Screens("/emailLogIn")
    data object EmailSignUp : Screens("/emailSignUp")
    data object ForgotPassword : Screens("/forgotPassword")

    data object GetSpotifyPlaylist : Screens("/getSpotifyPlaylist")
    data object SetBirthDate : Screens("/setBirthDate")
    data object PicGenre : Screens("/suggestGenre")
    data object PicArtist : Screens("/suggestArtist")

    data object AppRoot : Screens("/appRoot")

    data object HomeSearch : Screens("/homeSearch")
}
