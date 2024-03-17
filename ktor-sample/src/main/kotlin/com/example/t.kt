package com.example

import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

fun main() {
    val localTime = LocalDateTime.now().toLocalTime()

    val currentTime = localTime.format(DateTimeFormatter.ofPattern("hh")).toInt()
    val status = localTime.format(DateTimeFormatter.ofPattern("a")).uppercase()


    if (status == "AM") {
        if (currentTime == 12 || currentTime < 4) println("TimeType.MID_NIGHT")
        else if (currentTime < 10) println("TimeType.MORNING")
        else println("TimeType.DAY")
    } else {
        if (currentTime == 12 || currentTime < 6) println("DAY")
        else if (currentTime in 7..10) println("TimeType.NIGHT")
        else println("TimeType.MID_NIGHT")
    }
}
