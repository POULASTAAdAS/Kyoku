package com.poulastaa.core.domain.model

sealed class EndPoints(val route: String) {
    data object Auth : EndPoints(route = "/api/v1/auth")

    data object GetPasskey : EndPoints(route = "/api/v1/auth/getPasskeyRequest")
    data object CreatePasskeyUser : EndPoints(route = "/api/v1/auth/createPasskeyUser")
    data object GetPasskeyUser : EndPoints(route = "/api/v1/auth/getPasskeyUser")

    data object GetJWTToken : EndPoints(route = "/api/v1/auth/getJWTToken")
    data object RefreshToken : EndPoints(route = "/api/v1/auth/refreshToken")

    data object ForgotPassword : EndPoints(route = "/api/v1/auth/forgotPassword")

    data object ImportSpotifyPlaylist : EndPoints(route = "/api/v1/user/importSpotifyPlaylist")

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
    data object ViewOther : EndPoints(route = "/api/v1/view/viewOther")

    data object SyncLibrary : EndPoints(route = "/api/v1/sync/syncLibrary")

    sealed class Artist {
        data object GetArtist : EndPoints(route = "/api/v1/item/artist")
        data object GetArtistPagingSongs : EndPoints(route = "/api/v1/paging/getArtistPagingSongs")
        data object GetArtistPagingAlbums : EndPoints(route = "/api/v1/paging/getArtistPagingAlbums")
    }
}