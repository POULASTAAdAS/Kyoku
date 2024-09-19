package com.poulastaa.domain.model

sealed class EndPoints(val route: String) {
    data object GetLogInData : EndPoints(route = "/api/authorised/getLogInData")

    data object GetSpotifyPlaylistSong : EndPoints(route = "/api/authorised/spotifyPlaylist")

    data object GetCoverImage : EndPoints(route = "/api/authorised/coverImage")

    data object StoreBDate : EndPoints(route = "/api/authorised/storeBDate")

    data object SuggestGenre : EndPoints(route = "/api/authorised/suggestGenre")
    data object StoreGenre : EndPoints(route = "/api/authorised/storeGenre")

    data object SuggestArtist : EndPoints(route = "/api/authorised/suggestArtist")
    data object GetArtistImage : EndPoints(route = "/api/authorised/getArtistImage")
    data object StoreArtist : EndPoints(route = "/api/authorised/storeArtist")

    data object NewHome : EndPoints(route = "/api/authorised/newHome")

    data object AddToFavourite : EndPoints(route = "/api/authorised/addToFavourite")
    data object RemoveFromFavourite : EndPoints(route = "/api/authorised/removeFromFavourite")

    data object AddArtist : EndPoints(route = "/api/authorised/addArtist")
    data object RemoveArtist : EndPoints(route = "/api/authorised/removeArtist")

    data object GetAlbum : EndPoints(route = "/api/authorised/getAlbum")
    data object AddAlbum : EndPoints(route = "/api/authorised/addAlbum")
    data object RemoveAlbum : EndPoints(route = "/api/authorised/removeAlbum")

    data object SavePlaylist : EndPoints(route = "/api/authorised/savePlaylist")

    data object GetSong : EndPoints(route = "/api/authorised/getSong")
    data object UpdatePlaylist : EndPoints(route = "/api/authorised/updatePlaylist")
    data object UpdateFavourite : EndPoints(route = "/api/authorised/updateFavourite")

    data object CreatePlaylist : EndPoints(route = "/api/authorised/createPlaylist")

    data object PinData : EndPoints(route = "/api/authorised/pinData")
    data object UnPinData : EndPoints(route = "/api/authorised/unPinData")

    data object DeleteSavedData : EndPoints(route = "/api/authorised/deleteSavedData")

    data object GetTypeData : EndPoints(route = "/api/authorised/getTypeData")

    data object UnAuthorised : EndPoints(route = "/api/unauthorised")

    data object ViewArtist : EndPoints(route = "/api/authorised/viewArtist")

    data object GetArtist : EndPoints(route = "/api/authorised/getArtist")
    data object GetArtistSong : EndPoints(route = "/api/authorised/getArtistSong")
    data object GetArtistAlbum : EndPoints(route = "/api/authorised/getArtistAlbum")

    data object SyncData : EndPoints(route = "/api/authorised/syncData")

    data object GetAlbumPaging : EndPoints(route = "/api/authorised/getAlbumPaging")
    data object GetArtistPaging : EndPoints(route = "/api/authorised/getArtistPaging")

    data object GetCreatePlaylistData : EndPoints(route = "/api/authorised/getCreatePlaylistData")
    data object GetCreatePlaylistPagerData : EndPoints(route = "/api/authorised/getCreatePlaylistPagerData")

    data object GetSongArtist : EndPoints(route = "/api/authorised/getSongArtist")

    data object PlaySongMaster : EndPoints(route = "/api/authorised/playSong/master")
    data object PlaySongPlaylist : EndPoints(route = "/api/authorised/playSong/playlist")
    data object PlaySong : EndPoints(route = "/api/authorised/playSong/song")
}