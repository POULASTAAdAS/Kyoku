package com.poulastaa.suggestion.network.mapper

import com.poulastaa.core.domain.model.DtoAlbum
import com.poulastaa.core.domain.model.DtoSuggestedArtistSong
import com.poulastaa.core.network.mapper.*
import com.poulastaa.suggestion.domain.model.*
import com.poulastaa.suggestion.network.model.*

internal fun DtoAlbum.toResponsePrevAlbum() = ResponsePrevAlbum(
    id = this.id,
    name = this.name,
    poster = this.poster
)

internal fun DtoSuggestedArtistSong.toResponseSuggestedArtistSong() = ResponseSuggestedArtistSong(
    artist = this.artist.toPrevArtistRes(),
    prevSongs = this.prevSong.map { it.toResponsePrevSong() }
)

internal fun DtoRefresh.toResponseRefresh() = ResponseRefresh(
    prevPopularSongMix = this.prevPopularSongMix.map { it.toResponsePrevSong() },
    prevPopularArtistMix = this.prevPopularArtistMix.map { it.toResponsePrevSong() },
    prevOldGem = this.prevOldGem.map { it.toResponsePrevSong() },

    suggestedArtist = this.suggestedArtist.map { it.toPrevArtistRes() },
    suggestedAlbum = this.suggestedAlbum.map { it.toResponsePrevAlbum() },
    suggestedArtistSong = this.suggestedArtistSong.map { it.toResponseSuggestedArtistSong() }
)

internal fun DtoHome.toResponseHome() = ResponseHome(
    refresh = this.refresh.toResponseRefresh(),

    playlist = this.playlist.map { it.toResponsePlaylistFull() },
    album = this.album.map { it.toResponseFullAlbum() },
    artist = this.artist.map { it.toResponseArtist() }
)

internal fun RequestRefresh.toOldRefresh() = OldRefresh(
    oldMostPopularSong = this.oldMostPopularSong,
    oldPopularArtistSongs = this.oldPopularArtistSongs,
    oldPopularYearSongs = this.oldPopularYearSongs,
    oldSuggestedArtist = this.oldSuggestedArtist,
    oldSuggestedAlbums = this.oldSuggestedAlbums,
    oldSuggestedArtistSongs = this.oldSuggestedArtistSongs.map {
        OldSuggestedArtistSongRelation(
            artistId = it.artistId,
            prevSongs = it.prevSongs
        )
    }
)

internal fun DtoAddSongToPlaylistPageItem.toAddSongToPlaylistPageItemResponse() = AddSongToPlaylistPageItemResponse(
    type = when (this.type) {
        DtoAddSongToPlaylistPageType.YOUR_FAVOURITES -> AddSongToPlaylistPageTypeResponse.YOUR_FAVOURITES
        DtoAddSongToPlaylistPageType.SUGGESTED_FOR_YOU -> AddSongToPlaylistPageTypeResponse.SUGGESTED_FOR_YOU
        DtoAddSongToPlaylistPageType.YOU_MAY_ALSO_LIKE -> AddSongToPlaylistPageTypeResponse.YOU_MAY_ALSO_LIKE
    },
    data = this.data.map {
        AddSongToPlaylistItemResponse(
            id = it.id,
            title = it.title,
            poster = it.poster,
            artist = it.artist,
            numbers = it.numbers,
            type = when (it.type) {
                DtoAddToPlaylistItemType.PLAYLIST -> AddSongToPlaylistItemTypeResponse.PLAYLIST
                DtoAddToPlaylistItemType.ALBUM -> AddSongToPlaylistItemTypeResponse.ALBUM
                DtoAddToPlaylistItemType.ARTIST -> AddSongToPlaylistItemTypeResponse.ARTIST
                DtoAddToPlaylistItemType.SONG -> AddSongToPlaylistItemTypeResponse.SONG
            }
        )
    }
)