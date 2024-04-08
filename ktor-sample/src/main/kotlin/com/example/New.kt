package com.example

import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun main() {
    val input = "232Playlist 3923"

    println(!(input.matches(Regex("^\\W.*")) ||
                input.matches(Regex("^\\d.*"))))
}
