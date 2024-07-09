package com.poulastaa.play.presentation.root_drawer.home.mapper

import com.poulastaa.core.domain.model.Artist
import com.poulastaa.core.domain.model.DayType
import com.poulastaa.core.domain.model.HomeData
import com.poulastaa.core.domain.model.PrevAlbum
import com.poulastaa.core.domain.model.PrevArtistSong
import com.poulastaa.core.domain.model.PrevSong
import com.poulastaa.core.domain.model.PrevSongDetail
import com.poulastaa.core.presentation.ui.model.SpotifyUiArtist
import com.poulastaa.core.presentation.ui.model.UiArtist
import com.poulastaa.play.presentation.root_drawer.home.model.UiArtistWithSong
import com.poulastaa.play.presentation.root_drawer.home.model.UiHomeData
import com.poulastaa.play.presentation.root_drawer.home.model.UiPrevAlbum
import com.poulastaa.play.presentation.root_drawer.home.model.UiPrevSong
import com.poulastaa.play.presentation.root_drawer.home.model.UiSongWithInfo
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun getCurrentTime(): String {
    val localTime = LocalDateTime.now().toLocalTime()

    val currentTime = localTime.format(DateTimeFormatter.ofPattern("hh")).toInt()
    val status = localTime.format(DateTimeFormatter.ofPattern("a"))

    return if (status.uppercase() == "AM") {
        if (currentTime == 12) {
            "Mid Night"
        } else if (currentTime >= 4) {
            "Good Morning"
        } else {
            "Night Owl"
        }
    } else {
        if (currentTime <= 5 || currentTime == 12) {
            "Good Afternoon"
        } else if (currentTime in 6..10) {
            "Good Evening"
        } else if (currentTime in 10..11) {
            "Good Night"
        } else {
            "Night Owl"
        }
    }
}

fun getDayType(): DayType = when (getCurrentTime()) {
    "Good Morning" -> DayType.MORNING
    "Good Afternoon" -> DayType.DAY
    else -> DayType.NIGHT
}

private fun PrevSong.toUiPrevSong() = UiPrevSong(
    id = this.songId,
    coverImage = this.coverImage
)

private fun PrevAlbum.tUiPrevAlbum() = UiPrevAlbum(
    id = this.albumId,
    name = this.name,
    coverImage = this.coverImage
)

private fun Artist.toUiArtist() = UiArtist(
    id = this.id,
    name = this.name,
    coverImageUrl = this.coverImage ?: ""
)

private fun PrevSongDetail.toUiSongWithInfo() = UiSongWithInfo(
    id = this.songId,
    title = this.title,
    coverImage = this.coverImage
)

private fun PrevArtistSong.toUiArtistWithSong() = UiArtistWithSong(
    artist = this.artist.toUiArtist(),
    listOfSong = this.songs.map {
        it.toUiSongWithInfo()
    }
)

fun HomeData.toUiHomeData() = UiHomeData(
    popularSongMix = this.popularSongMixPrev.map {
        it.toUiPrevSong()
    },
    popularSongFromYourTime = this.popularSongFromYourTimePrev.map {
        it.toUiPrevSong()
    },
    favouriteArtistMix = this.favouriteArtistMixPrev.map {
        it.toUiPrevSong()
    },
    dayTypeSong = this.dayTypeSong.map {
        it.toUiPrevSong()
    },
    popularAlbum = this.popularAlbum.map {
        it.tUiPrevAlbum()
    },
    suggestedArtist = this.suggestedArtist.map {
        it.toUiArtist()
    },
    popularArtistSong = this.popularArtistSong.map {
        it.toUiArtistWithSong()
    }
)

