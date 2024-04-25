package com.example

import kotlinx.coroutines.delay

import kotlinx.coroutines.delay

fun millisecondsToMinutesAndSeconds(milliseconds: Long): String {
    val totalSeconds = milliseconds / 1000.0
    val minutes = (totalSeconds / 60).toLong()
    val seconds = (totalSeconds % 60).toLong()
    return "$minutes:$seconds"
}


suspend fun main() {
    for (i in 0..60000L) {
        println(millisecondsToMinutesAndSeconds(i))
    }
}
