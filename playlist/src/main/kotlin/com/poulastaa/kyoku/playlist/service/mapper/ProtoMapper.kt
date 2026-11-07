package com.poulastaa.kyoku.playlist.service.mapper

import com.poulastaa.kyoku.grpc.gateway_playlist.ResponsePlaylist
import com.poulastaa.kyoku.grpc.gateway_playlist.ResponseSong
import com.poulastaa.kyoku.grpc.model.*
import com.poulastaa.kyoku.playlist.database.playlist.entity.EntityPlaylist
import com.poulastaa.kyoku.playlist.domain.model.*

// protobuf setters reject null: every nullable field is set through `?.let`

fun EntityPlaylist.toResponsePlaylist(): ResponsePlaylist = ResponsePlaylist.newBuilder().apply {
    playlistId = this@toResponsePlaylist.id
    name = this@toResponsePlaylist.name
    popularity = this@toResponsePlaylist.popularity
    status = when (visibility) {
        true -> ResponsePlaylist.ResponsePlaylistVisibilityState.PUBLIC
        false -> ResponsePlaylist.ResponsePlaylistVisibilityState.PRIVATE
    }
}.build()

fun DtoSong.toResponseSong(): ResponseSong = ResponseSong.newBuilder().apply {
    songId = this@toResponseSong.id
    title = this@toResponseSong.title
    this@toResponseSong.poster?.let { poster = it }
    masterPlaylist = this@toResponseSong.masterPlaylist
    info = this@toResponseSong.info.toSongInfo(this@toResponseSong.id)
    addAllArtists(artists.map { it.toArtist(withRelations = true) })
    addAllGenre(this@toResponseSong.genre.map { it.toGenre() })
    addAllCountry(this@toResponseSong.country.map { it.toCountry() })
    addAllAlbums(album.map { it.toAlbum() })
}.build()

private fun DtoSongInfo.toSongInfo(songId: Long): SongInfo = SongInfo.newBuilder().apply {
    this.songId = songId
    releaseYear = this@toSongInfo.releaseYear
    addAllComposers(composer.map { it.toComposer() })
    popularity = this@toSongInfo.popularity
}.build()

private fun DtoComposer.toComposer(): Composer = Composer.newBuilder().apply {
    id = this@toComposer.id
    name = this@toComposer.name
    this@toComposer.coverImage?.let { coverImage = it }
    followers = this@toComposer.followers
}.build()

private fun DtoArtist.toArtist(withRelations: Boolean): Artist = Artist.newBuilder().apply {
    id = this@toArtist.id
    name = this@toArtist.name
    this@toArtist.coverImage?.let { coverImage = it }
    followers = this@toArtist.followers
    this@toArtist.birthDate?.let { birthDate = it.toString() }
    this@toArtist.biography?.let { biography = it }
    monthlyListeners = this@toArtist.monthlyListeners

    if (withRelations) {
        addAllAlbums(this@toArtist.albums.map { it.toAlbum() })
        addAllGenres(this@toArtist.genres.map { it.toGenre() })
    }
}.build()

private fun DtoAlbum.toAlbum(): Album = Album.newBuilder().apply {
    id = this@toAlbum.id
    name = this@toAlbum.name
    popularity = this@toAlbum.popularity
    this@toAlbum.poster?.let { poster = it }
    addAllArtists(this@toAlbum.artists.map { it.toArtist(withRelations = false) })
}.build()

private fun DtoGenre.toGenre(): Genre = Genre.newBuilder().apply {
    id = this@toGenre.id
    name = this@toGenre.name
    this@toGenre.coverImage?.let { coverImage = it }
    popularity = this@toGenre.popularity
}.build()

private fun DtoCountry.toCountry(): Country = Country.newBuilder().apply {
    id = this@toCountry.id
    country = this@toCountry.country
    code = this@toCountry.code
}.build()
