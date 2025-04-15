package com.poulastaa.core.domain.model

sealed class EndPoints(val route: String) {
    private object VERSIONS {
        const val ONE = "v1"
    }

    object Auth : EndPoints(route = "/api/${VERSIONS.ONE}/auth")

    data object GetPasskey : EndPoints(route = "/api/${VERSIONS.ONE}/auth/getPasskeyRequest")
    data object CreatePasskeyUser : EndPoints(route = "/api/${VERSIONS.ONE}/auth/createPasskeyUser")
    data object GetPasskeyUser : EndPoints(route = "/api/${VERSIONS.ONE}/auth/getPasskeyUser")

    data object GetJWTToken : EndPoints(route = "/api/${VERSIONS.ONE}/auth/getJWTToken")
    data object RefreshToken : EndPoints(route = "/api/${VERSIONS.ONE}/auth/refreshToken")

    data object ForgotPassword : EndPoints(route = "/api/${VERSIONS.ONE}/auth/forgotPassword")

    data object ImportSpotifyPlaylist : EndPoints(
        route = "/api/${VERSIONS.ONE}/user/importSpotifyPlaylist"
    )

    data object SetBDate : EndPoints(route = "/api/${VERSIONS.ONE}/user/setBDate")

    data object SuggestGenre : EndPoints(route = "/api/${VERSIONS.ONE}/user/suggestGenre")
    data object UPSERTGenre : EndPoints(route = "/api/${VERSIONS.ONE}/user/upsertGenre")

    data object SuggestArtist : EndPoints(route = "/api/${VERSIONS.ONE}/user/suggestArtist")
    data object UPSERTArtist : EndPoints(route = "/api/${VERSIONS.ONE}/user/upsertArtist")

    data object Home : EndPoints(route = "/api/${VERSIONS.ONE}/suggestion/home")
    data object RefreshHome : EndPoints(route = "/api/${VERSIONS.ONE}/suggestion/refreshHome")

    data object GetBDate : EndPoints(route = "/api/${VERSIONS.ONE}/user/getBDate")
    data object UpdateUsername : EndPoints(route = "/api/${VERSIONS.ONE}/user/updateUsername")

    data object ViewArtist : EndPoints(route = "/api/${VERSIONS.ONE}/view/viewArtist")
    data object ViewOther : EndPoints(route = "/api/${VERSIONS.ONE}/view/viewOther")

    data object SyncLibrary : EndPoints(route = "/api/${VERSIONS.ONE}/sync/syncLibrary")

    sealed class Artist {
        data object GetArtist : EndPoints(route = "/api/${VERSIONS.ONE}/item/artist")
        data object GetArtistPagingSongs : EndPoints(
            route = "/api/${VERSIONS.ONE}/paging/getArtistPagingSongs"
        )

        data object GetArtistPagingAlbums : EndPoints(
            "/api/${VERSIONS.ONE}/paging/getArtistPagingAlbums"
        )

        data object GetPagingArtist : EndPoints(
            route = "/api/${VERSIONS.ONE}/paging/getPagingArtist"
        )
    }

    sealed class Playlist {
        data object CreatePlaylist : EndPoints(
            route = "/api/${VERSIONS.ONE}/item/playlist/createPlaylist"
        )
    }

    sealed class Album {
        data object GetPagingAlbum : EndPoints(route = "/api/${VERSIONS.ONE}/paging/getPagingAlbum")
    }

    sealed class Add {
        data object AddSong : EndPoints(route = "/api/${VERSIONS.ONE}/item/playlist/addSong")
        data object GetCreatePlaylist : EndPoints(
            route = "/api/${VERSIONS.ONE}/paging/getCreatePlaylist"
        )

        sealed class Playlist {
            data object CreatePlaylistStaticData : EndPoints(
                route = "/api/${VERSIONS.ONE}/suggestion/createPlaylistStaticData"
            )
        }
    }
}