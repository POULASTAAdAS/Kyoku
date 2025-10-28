package com.poulastaa.core.network

import com.poulastaa.core.domain.model.DtoArtist
import com.poulastaa.core.domain.model.DtoFullPlaylist
import com.poulastaa.core.domain.model.DtoJWTToken
import com.poulastaa.core.domain.model.DtoSong
import com.poulastaa.core.network.domain.model.response.ResponseArtist
import com.poulastaa.core.network.domain.model.response.ResponseFullPlaylist
import com.poulastaa.core.network.domain.model.response.ResponseJWTToken
import com.poulastaa.core.network.domain.model.response.ResponseSong

fun ResponseJWTToken.toDtoJWTToken() = DtoJWTToken(
    access = accessToken,
    refresh = refreshToken
)

fun ResponseArtist.toDtoArtist() = DtoArtist(
    artistId = this.artistId,
    name = this.name,
    coverImage = this.poster,
    popularity = this.popularity
)

fun ResponseSong.toDtoSong() = DtoSong(
    songId = this.songId,
    title = this.title,
    coverImage = this.coverImage,
    artists = this.artist.map { it.toDtoArtist() }
)

fun ResponseFullPlaylist.toDtoFullPlaylist() = DtoFullPlaylist(
    playlistId = this.playlistId,
    title = this.name,
    songs = this.songs.map { it.toDtoSong() }
)
