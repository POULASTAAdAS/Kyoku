package com.poulastaa.main.network.mapper

import com.poulastaa.core.domain.model.DtoFullAlbum
import com.poulastaa.core.domain.model.DtoPrevAlbum
import com.poulastaa.core.domain.model.DtoPrevSong
import com.poulastaa.core.network.mapper.toDtoAlbum
import com.poulastaa.core.network.mapper.toDtoArtist
import com.poulastaa.core.network.mapper.toDtoFullPlaylist
import com.poulastaa.core.network.mapper.toDtoPrevArtist
import com.poulastaa.core.network.mapper.toDtoSong
import com.poulastaa.core.network.model.ResponseFullAlbum
import com.poulastaa.core.network.model.ResponsePrevAlbum
import com.poulastaa.core.network.model.ResponsePrevSong
import com.poulastaa.main.domain.model.DtoHome
import com.poulastaa.main.domain.model.DtoRefresh
import com.poulastaa.main.domain.model.DtoSuggestedArtistSong
import com.poulastaa.main.network.model.ResponseHome
import com.poulastaa.main.network.model.ResponseRefresh
import com.poulastaa.main.network.model.ResponseSuggestedArtistSong

internal fun ResponsePrevSong.toDtoPrevSong() = DtoPrevSong(
    id = this.id,
    title = this.title,
    poster = this.poster
)

internal fun ResponsePrevAlbum.toDtoPrevAlbum() = DtoPrevAlbum(
    id = this.id,
    name = this.name,
    poster = this.poster
)

internal fun ResponseSuggestedArtistSong.toDtoSuggestedArtistSong() = DtoSuggestedArtistSong(
    artist = this.artist.toDtoPrevArtist(),
    prevSong = this.prevSongs.map { it.toDtoPrevSong() }
)

internal fun ResponseRefresh.toDtoRefresh() = DtoRefresh(
    prevPopularSongMix = this.prevPopularSongMix.map { it.toDtoPrevSong() },
    prevPopularArtistMix = this.prevPopularArtistMix.map { it.toDtoPrevSong() },
    prevOldGem = this.prevOldGem.map { it.toDtoPrevSong() },

    suggestedArtist = this.suggestedArtist.map { it.toDtoPrevArtist() },
    suggestedAlbum = this.suggestedAlbum.map { it.toDtoPrevAlbum() },
    suggestedArtistSong = this.suggestedArtistSong.map { it.toDtoSuggestedArtistSong() }
)

internal fun ResponseFullAlbum.toDtoFullAlbum() = DtoFullAlbum(
    album = this.album.toDtoAlbum(),
    songs = this.songs.map { it.toDtoSong() }
)

internal fun ResponseHome.toDtoHome() = DtoHome(
    refresh = this.refresh.toDtoRefresh(),
    playlist = this.playlist.map { it.toDtoFullPlaylist() },
    album = this.album.map { it.toDtoFullAlbum() },
    artist = this.artist.map { it.toDtoArtist() }
)