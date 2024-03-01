package com.poulastaa.utils.songDownloaderApi

import com.poulastaa.data.model.setup.spotify.SpotifySongDownloaderApiReq

suspend fun SpotifySongDownloaderApiReq.makeApiCallOnNotFoundSpotifySongs() {
    println(this.listOfSong) //todo
}