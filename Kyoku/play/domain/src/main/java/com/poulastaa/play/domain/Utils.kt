package com.poulastaa.play.domain

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