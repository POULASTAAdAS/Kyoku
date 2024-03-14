package com.example

import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

fun main() {
    val localTIme = LocalTime.now()
    println(localTIme)

    val time = LocalTime.now().format(DateTimeFormatter.ofPattern("hh a"))

    println(time)
}
