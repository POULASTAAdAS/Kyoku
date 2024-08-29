package com.poulastaa.play.domain

enum class ViewSongOperation(val value: String) {
    PLAY("play"),
    PLAY_NEXT("play next"),
    PLAY_LAST("play last"),
    ADD_TO_PLAYLIST("add to playlist"),
    ADD_TO_FAVOURITE("add to favourite"),
    VIEW_ARTISTS("view artists")
}