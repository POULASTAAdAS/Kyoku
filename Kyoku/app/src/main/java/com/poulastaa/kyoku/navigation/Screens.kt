package com.poulastaa.kyoku.navigation

sealed class Screens(val route: String) {
    enum class Args(val title: String) {
        TYPE("type"),
        ID("id"),
        NAME("name"),
        IS_API_CALL("isApiCall"),
        IS_FOR_MORE("isForMore")
    }

    data object Auth : Screens("/auth")

    data object AuthEmailLogin : Screens("/auth/emailLogin")
    data object AuthEmailSignUp : Screens("/auth/emailSignUp")

    data object ForgotPassword : Screens("/auth/forgotPassword")

    data object GetSpotifyPlaylist : Screens("/setup/getSpotifyPlaylist")
    data object SetBirthDate : Screens("/setup/setBirthDate")
    data object SuggestGenre : Screens("/setup/suggestGenre")
    data object SuggestArtist : Screens("/setup/suggestArtist")

    data object SongView : Screens("/app/songView/") {
        const val PARAMS: String = "{type}/{id}/{name}/{isApiCall}"
    }

    data object AllFromArtist : Screens("/app/songView/allFromArtist/") {
        const val PARAMS: String = "{name}/{isForMore}"
    }

    data object Player : Screens("/app/songView/player")

    data object HomeRoot : Screens("/app/homeRoot")

    data object Home : Screens("/app/homeRoot/home")

    data object Library : Screens("/app/homeRoot/library")


    data object CreatePlaylist : Screens("/app/createPlaylist") {
        const val PARAMS: String = "{name}/{type}"
    }

    data object AddArtist : Screens("/app/addArtist")
    data object AddAlbum : Screens("/app/addAlbum")

    data object Profile : Screens("/app/profile")
    data object History : Screens("/app/history")
    data object Settings : Screens("/app/settings")

    data object Search : Screens("/route/search")
}