package com.poulastaa.suggestion.network.mapper

import com.poulastaa.core.domain.model.DtoAlbum
import com.poulastaa.core.domain.model.DtoSuggestedArtistSong
import com.poulastaa.core.network.mapper.*
import com.poulastaa.suggestion.domain.model.DtoHome
import com.poulastaa.suggestion.domain.model.DtoRefresh
import com.poulastaa.suggestion.domain.model.OldRefresh
import com.poulastaa.suggestion.domain.model.OldSuggestedArtistSongRelation
import com.poulastaa.suggestion.network.domain.*
import com.poulastaa.suggestion.network.domain.ResponseHome
import com.poulastaa.suggestion.network.domain.ResponseRefresh
import com.poulastaa.suggestion.network.domain.ResponseSuggestedArtistSong

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