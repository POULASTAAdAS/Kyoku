package com.poulastaa.data.mappers

import com.poulastaa.data.dao.AlbumDao
import com.poulastaa.data.dao.ArtistDao
import com.poulastaa.data.dao.SongDao
import com.poulastaa.data.model.*
import com.poulastaa.domain.model.ResultArtist

fun ArtistDao.toArtistResult() = ResultArtist(
    id = this.id.value,
    name = this.name,
    profilePic = this.constructProfilePic() ?: ""
)

fun SongDao.toSongDto(artist: String) = SongDto(
    id = this.id.value,
    coverImage = this.constructCoverImage(),
    title = this.title,
    artistName = artist,
    releaseYear = this.year,
    masterPlaylistUrl = this.constructMasterPlaylistUrl()
)

fun AlbumDao.toAlbum(coverImage: String) = AlbumDto(
    id = this.id.value,
    name = this.name,
    coverImage = coverImage,
)


fun SongDto.toCreatePlaylistPagingDto() = CreatePlaylistPagingDto(
    id = this.id,
    title = this.title,
    artist = this.artistName,
    coverImage = coverImage,
    expandable = false,
    isArtist = false
)

fun ArtistDto.toCreatePlaylistPagingDto() = CreatePlaylistPagingDto(
    id = this.id,
    title = this.name,
    coverImage = this.coverImage ?: "",
    expandable = true,
    isArtist = true
)

fun PagingAlbumDto.toCreatePlaylistPagingDto() = CreatePlaylistPagingDto(
    id = this.id,
    title = this.name,
    coverImage = this.coverImage,
    expandable = true,
    isArtist = false
)
