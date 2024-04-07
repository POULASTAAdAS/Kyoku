package com.example

import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun main() {
    val date = LocalDate.now().format(DateTimeFormatter.ofPattern("dd:MM:yy"))

    println(date)
}