package com.example

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun main() {
    val current = System.currentTimeMillis()

    val instant = Instant.ofEpochMilli(current)

    val date = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())

    println(date.year - 4)

    println(date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
}